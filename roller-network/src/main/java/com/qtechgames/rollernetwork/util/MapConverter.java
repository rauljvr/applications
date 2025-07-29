package com.qtechgames.rollernetwork.util;

import com.qtechgames.rollernetwork.dto.RollerResponse;
import com.qtechgames.rollernetwork.model.RollerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MapConverter {

    private ModelMapper modelMapper;

    @Autowired
    public MapConverter(@Lazy ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RollerResponse convertToDto(RollerEntity rollerEntity) {
        return modelMapper.map(rollerEntity, RollerResponse.class);
    }

    public RollerEntity convertToEntity(RollerResponse roller) {
        return modelMapper.map(roller, RollerEntity.class);
    }
}
