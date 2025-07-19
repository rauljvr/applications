package com.qtechgames.rollernetwork.service;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.model.RollerEntity;

import java.util.List;
import java.util.Optional;

public interface IRollerService {

    public List<String> getRollerDownline(String name);

    public RollerEntity createRoller(RollerDTO roller);
}
