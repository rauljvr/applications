package com.qtechgames.rollernetwork.service;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.model.RollerEntity;

import java.util.List;

public interface IRollerService {

    public RollerEntity getRollerById(Long id);

    public RollerEntity getRollerByName(String name);

    public List<String> getRollerDownline(final String name);

    public RollerEntity createRoller(final RollerDTO roller);

    public RollerEntity getRollerReferral(final String name);

    public void deleteRoller(final String name);

    public RollerEntity updateParentRoller(final String name, final RollerDTO roller);
}
