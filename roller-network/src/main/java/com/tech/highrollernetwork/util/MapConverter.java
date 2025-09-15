package com.tech.highrollernetwork.util;

import com.tech.highrollernetwork.dto.PlayerResponse;
import com.tech.highrollernetwork.model.PlayerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class MapConverter {

    private ModelMapper modelMapper;

    public MapConverter(@Lazy ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PlayerResponse convertToDto(PlayerEntity rollerEntity) {
        return modelMapper.map(rollerEntity, PlayerResponse.class);
    }

    public PlayerEntity convertToEntity(PlayerResponse roller) {
        return modelMapper.map(roller, PlayerEntity.class);
    }
}
