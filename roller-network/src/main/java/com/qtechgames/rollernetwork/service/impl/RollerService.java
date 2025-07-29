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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return rollerRepository.findByName(rollerName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Roller's name not found: " + rollerName));
    }

    @Override
    public List<String> getRollerDownline(final String name) {
        RollerEntity roller = this.findRollerByName(name, "Roller's name not found on the network: ");

        List<String> downlineList = new ArrayList<>();
        downline(downlineList, roller);

        return downlineList;
    }

    @Override
    public RollerEntity createRoller(final RollerDTO rollerDTO) {
        Optional<RollerEntity> roller = rollerRepository.findByName(rollerDTO.getName().toUpperCase());

        if (roller.isPresent() && Boolean.FALSE.equals(roller.get().getExit())) {
            throw new ResourceAlreadyExistsException("Roller already exists on the network: " + rollerDTO.getName());
        }
        if (roller.isPresent()) {
            throw new ResourceAlreadyExistsException("Roller already left the network: " + rollerDTO.getName());
        }

        RollerEntity referralRoller = this.findRollerByName(rollerDTO.getParentName(), "Referral roller not found: ");
        RollerEntity rollerEntity = RollerEntity.builder()
                .name(rollerDTO.getName().toUpperCase())
                .parentId(referralRoller.getId())
                .referralChain(String.valueOf(referralRoller.getId()))
                .exit(Boolean.FALSE)
                .build();

        return rollerRepository.save(rollerEntity);
    }

    @Override
    public RollerEntity getRollerReferral(final String name) {
        RollerEntity roller = this.findRollerByName(name, "Roller's name not found on the network: ");

        if (Objects.isNull(roller.getReferralChain())) {
            throw new ResourceNotFoundException("The roller does not have any referral: " + name);
        }

        Long referralId = Long.valueOf(roller.getReferralChain().split(",")[0]);
        return this.findRollerById(referralId, "Referral Roller not found on the network: ");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RollerEntity rollerExit(final String name) {
        RollerEntity roller = this.findRollerByName(name, "Roller's name not found: ");
        if ("casino".equalsIgnoreCase(name) && (!roller.getExit())) {
            allExit();
            return roller;
        }

        roller.setExit(Boolean.TRUE);
        List<RollerEntity> children = rollerRepository.findByParentId(roller.getId());

        if (!children.isEmpty()) {
            children.forEach(child -> {
                RollerEntity newParent = findRollerById(roller.getParentId(), "Roller's ID not found: ");
                rollerTransfer(child.getName(), newParent.getName());
            });
        }

        return rollerRepository.save(roller);
    }

    private void allExit() {
        rollerRepository.findAll().forEach(roller -> {
            roller.setExit(Boolean.TRUE);
            rollerRepository.save(roller);
        });
    }

    @Override
    public RollerEntity rollerTransfer(final String rollerName, final String referralName) {
        RollerEntity roller = this.findRollerByName(rollerName, "Roller's name not found: ");
        RollerEntity referral = this.findRollerByName(referralName, "New VIP host not found: ");

        roller.setParentId(referral.getId());
        roller.setReferralChain(roller.getReferralChain() + "," + referral.getId());

        return rollerRepository.save(roller);
    }

    private RollerEntity findRollerById(Long rollerId, String message) {
        return rollerRepository.findById(rollerId)
                .orElseThrow(() -> new ResourceNotFoundException(message + rollerId));
    }

    private RollerEntity findRollerByName(String rollerName, String message) {
        Optional<RollerEntity> roller = rollerRepository.findByName(rollerName.toUpperCase());
        if (roller.isEmpty() || Boolean.TRUE.equals(roller.get().getExit())) {
            throw new ResourceNotFoundException(message + rollerName);
        }

        return roller.get();
    }

    private void downline(List<String> downlineList, RollerEntity roller) {
        if (Boolean.FALSE.equals(roller.getExit())) {
            downlineList.add(roller.getName());
        }

        List<RollerEntity> children = rollerRepository.findByParentId(roller.getId());
        if (children.isEmpty()) {
            return;
        }

        children.forEach(child -> downline(downlineList, child));
    }

}
