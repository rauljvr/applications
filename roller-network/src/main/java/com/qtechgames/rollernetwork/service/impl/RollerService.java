package com.qtechgames.rollernetwork.service.impl;

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
    public List<String> getRollerDownline() {

        List<String> downline = new ArrayList<>();
        //List<RollerEntity> rollers = rollerRepository.findAllByOrderByDepthAsc();
        List<RollerEntity> rollers = rollerRepository.findByDepthLessThan(2L);

        if (rollers.isEmpty()) {
            throw new ResourceNotFoundException("No Rollers found");
        }

        downline.add(rollers.get(0).getName());
        rollers.stream()
                .filter(roller -> !roller.getId().equals(0L))
                .forEach(roller -> {
                    downline.add(roller.getName());
                    List<RollerEntity> children = rollerRepository.findByParentId(roller.getId());
                    children.forEach(child -> {
                        downline.add(child.getName());
                        rollerRepository.findByParentId(child.getId())
                                .forEach(leafNode -> downline.add(leafNode.getName()));
                    });
                });

        return downline;
    }

}
