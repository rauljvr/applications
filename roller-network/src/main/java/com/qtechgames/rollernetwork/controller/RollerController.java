package com.qtechgames.rollernetwork.controller;

import com.qtechgames.rollernetwork.model.RollerEntity;
import com.qtechgames.rollernetwork.service.IRollerService;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/rollernetwork")
public class RollerController {

    private IRollerService rollerService;

    public RollerController(@Lazy IRollerService rollerService) {
        this.rollerService = rollerService;
    }

    @GetMapping("/")
    public ResponseEntity<List<String>> getRollerDownline() {
        log.debug("Getting tree downline");

        return ResponseEntity.ok().body(rollerService.getRollerDownline());
    }
}
