package com.tech.highrollernetwork.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRequest {

    @NotNull
    @Size(min=1, max = 50)
    private String name;

    @NotNull
    @Size(min=1, max = 50)
    private String parentName;

}
