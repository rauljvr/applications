package com.qtechgames.rollernetwork.service;

import com.qtechgames.rollernetwork.model.RollerEntity;

import java.util.Optional;

public interface IRollerService {

    public Optional<RollerEntity> getRoller(String name);
}
