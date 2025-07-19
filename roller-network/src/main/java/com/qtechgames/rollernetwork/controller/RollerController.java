package com.qtechgames.rollernetwork.controller;

import com.qtechgames.rollernetwork.model.RollerEntity;
import com.qtechgames.rollernetwork.service.IRollerService;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/rollernetwork")
public class RollerController {

    private IRollerService rollerService;

    public RollerController(@Lazy IRollerService rollerService) {
        this.rollerService = rollerService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<RollerEntity> getRoller(@PathVariable(value = "name") String name) {
        log.debug("Getting player's info: {}", name);

        return ResponseEntity.ok().body(rollerService.getRoller(name).get());
    }
}
