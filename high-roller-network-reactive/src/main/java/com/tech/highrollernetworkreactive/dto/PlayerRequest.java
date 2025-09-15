package com.tech.highrollernetworkreactive.dto;

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
    @Size(min=1, max = 20)
    private String name;

    @NotNull
    @Size(min=1, max = 20)
    private String parentName;

}
