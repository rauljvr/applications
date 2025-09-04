package com.tech.highrollernetwork.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerResponse {

    private Long id;
    private String name;
    private Long parentId;
    private String referralChain;
    private Boolean exit;
}
