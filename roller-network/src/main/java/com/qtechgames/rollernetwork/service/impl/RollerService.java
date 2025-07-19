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
import java.util.Optional;

@Slf4j
@Service
public class RollerService implements IRollerService {

    private final RollerRepository rollerRepository;

    public RollerService(@Lazy RollerRepository rollerRepository) {
        this.rollerRepository = rollerRepository;
    }

    @Override
    public List<String> getRollerDownline(final String name) {
        List<String> downlineList = new ArrayList<>();
        RollerEntity roller = rollerRepository.findByName(name.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Roller name not found: " + name));

        downline(downlineList, roller);

        return downlineList;
    }

    @Override
    public RollerEntity createRoller(final RollerDTO rollerDTO) {
        rollerRepository.findByName(rollerDTO.getName().toUpperCase()).ifPresent(r -> {
            throw new ResourceAlreadyExistsException("Roller already exists on the network: " + rollerDTO.getName());
        });

        RollerEntity referralRoller = rollerRepository.findByName(rollerDTO.getReferralName().toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Referral roller not found: " + rollerDTO.getReferralName()));

        RollerEntity rollerEntity = RollerEntity.builder()
                .name(rollerDTO.getName().toUpperCase())
                .parentId(referralRoller.getId())
                .referralId(referralRoller.getId())
                .build();

        return rollerRepository.save(rollerEntity);
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
