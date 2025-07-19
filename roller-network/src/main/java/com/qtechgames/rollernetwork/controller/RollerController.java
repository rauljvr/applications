package com.qtechgames.rollernetwork.controller;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.model.RollerEntity;
import com.qtechgames.rollernetwork.service.IRollerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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

    @GetMapping("/roller/{name}/downline")
    public ResponseEntity<List<String>> getRollerDownline(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Getting downline of the Roller: {}", name);

        return ResponseEntity.ok().body(rollerService.getRollerDownline(name));
    }

    @PostMapping("/roller")
    public ResponseEntity<RollerEntity> createRoller(@RequestBody @Valid RollerDTO roller) {
        log.debug("Creating Roller: {}", roller.getName());
        RollerEntity rollerSaved = rollerService.createRoller(roller);

        return ResponseEntity.created(URI.create("/roller/" + rollerSaved.getId()))
                .body(rollerSaved);
    }
}
