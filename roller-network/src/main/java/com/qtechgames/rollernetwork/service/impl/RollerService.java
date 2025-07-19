package com.qtechgames.rollernetwork.service.impl;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.exceptionhandler.ResourceAlreadyExistsException;
import com.qtechgames.rollernetwork.exceptionhandler.ResourceNotFoundException;
import com.qtechgames.rollernetwork.model.RollerEntity;
import com.qtechgames.rollernetwork.repository.RollerRepository;
import com.qtechgames.rollernetwork.service.IRollerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class RollerService implements IRollerService {

    private final RollerRepository rollerRepository;

    public RollerService(@Lazy RollerRepository rollerRepository) {
        this.rollerRepository = rollerRepository;
    }

    @Override
    public RollerEntity getRollerById(final Long rollerId) {
        return this.findRollerById(rollerId, "Roller ID not found: ");
    }

    @Override
    public RollerEntity getRollerByName(final String rollerName) {
        return this.findRollerByName(rollerName, "Roller's name not found: ");
    }

    @Override
    public List<String> getRollerDownline(final String name) {
        List<String> downlineList = new ArrayList<>();
        RollerEntity roller = this.findRollerByName(name, "Roller's name not found: ");

        downline(downlineList, roller);

        return downlineList;
    }

    @Override
    public RollerEntity createRoller(final RollerDTO rollerDTO) {
        rollerRepository.findByName(rollerDTO.getName().toUpperCase()).ifPresent(r -> {
            throw new ResourceAlreadyExistsException("Roller already exists on the network: " + rollerDTO.getName());
        });

        RollerEntity referralRoller = this.findRollerByName(rollerDTO.getParentName(), "Referral roller not found: ");

        RollerEntity rollerEntity = RollerEntity.builder()
                .name(rollerDTO.getName().toUpperCase())
                .parentId(referralRoller.getId())
                .referralId(referralRoller.getId())
                .build();

        return rollerRepository.save(rollerEntity);
    }

    @Override
    public RollerEntity getRollerReferral(final String rollerName) {
        RollerEntity roller = this.findRollerByName(rollerName, "Roller's name not found: ");

        if (Objects.isNull(roller.getReferralId())) {
            throw new ResourceNotFoundException("The referral roller of " + rollerName + " already left the network.");
        }

        return this.findRollerById(roller.getReferralId(), "Referral Roller not found: ");
    }

    @Override
    public void deleteRoller(final String name) {
        RollerEntity parentRoller = this.findRollerByName(name, "Roller's name not found: ");
        List<RollerEntity> children = rollerRepository.findByParentId(parentRoller.getId());

        if (!children.isEmpty()) {
            children.forEach(child -> {
                child.setParentId(parentRoller.getParentId());
                child.setReferralId(null);
                rollerRepository.save(child);
            });
        }

        rollerRepository.delete(parentRoller);
    }

    @Override
    public RollerEntity updateParentRoller(final String name, final RollerDTO rollerDTO) {
        RollerEntity roller = this.findRollerByName(name, "Roller's name not found: ");
        RollerEntity referralRoller = this.findRollerByName(rollerDTO.getParentName(), "New VIP host not found: ");

        roller.setName(rollerDTO.getName().toUpperCase());
        roller.setParentId(referralRoller.getId());

        return rollerRepository.save(roller);
    }

    private RollerEntity findRollerById(Long rollerId, String message) {
        return rollerRepository.findById(rollerId)
                .orElseThrow(() -> new ResourceNotFoundException(message + rollerId));
    }

    private RollerEntity findRollerByName(String rollerName, String message) {
        return rollerRepository.findByName(rollerName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException(message + rollerName));
    }

    private void downline(List<String> downlineList, RollerEntity roller) {
        downlineList.add(roller.getName());
        List<RollerEntity> children = rollerRepository.findByParentId(roller.getId());

        if (children.isEmpty()) {
            return;
        }

        children.forEach(child -> downline(downlineList, child));
    }

}
