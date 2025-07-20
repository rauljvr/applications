package com.qtechgames.rollernetwork.controller;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.dto.RollerTransferDTO;
import com.qtechgames.rollernetwork.model.RollerEntity;
import com.qtechgames.rollernetwork.service.IRollerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@Tag(name = "Roller Management", description = "This Rest API provides the services to manage the online casino and its network of players.")
public class RollerController {

    private IRollerService rollerService;

    @GetMapping("/roller/{id}")
    public ResponseEntity<RollerEntity> getRollerById(@PathVariable(value = "id") Long id) {
        log.debug("Getting the Roller by ID: {}", id);

        return ResponseEntity.ok().body(rollerService.getRollerById(id));
    }

    @GetMapping("/roller")
    public ResponseEntity<RollerEntity> getRollerByName(@RequestParam(value = "name") @Size(max = 50) String name) {
        log.debug("Getting the Roller: {}", name);

        return ResponseEntity.ok().body(rollerService.getRollerByName(name));
    }

    public RollerController(@Lazy IRollerService rollerService) {
        this.rollerService = rollerService;
    }

    @GetMapping("/roller/{name}/downline")
    public ResponseEntity<List<String>> getRollerDownline(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Getting downline of the Roller: {}", name);

        return ResponseEntity.ok().body(rollerService.getRollerDownline(name));
    }

    @GetMapping("/roller/{name}/referral")
    public ResponseEntity<String> getRollerReferral(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Getting the Roller referral of: {}", name);

        return ResponseEntity.ok().body(rollerService.getRollerReferral(name).getName());
    }

    @PostMapping("/roller")
    public ResponseEntity<RollerEntity> createRoller(@RequestBody @Valid RollerDTO roller) {
        log.debug("Creating Roller: {}", roller.getName());
        RollerEntity rollerSaved = rollerService.createRoller(roller);

        return ResponseEntity.created(URI.create("/roller/" + rollerSaved.getId()))
                .body(rollerSaved);
    }

    @PutMapping("/roller/{name}/exit")
    public ResponseEntity<RollerEntity> rollerExit(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Deleting Roller: {}", name);

        return ResponseEntity.ok().body(rollerService.rollerExit(name));
    }

    @PutMapping("/roller/{name}/transfer")
    public ResponseEntity<RollerEntity> rollerTransfer(@PathVariable(value = "name") @Size(max = 50) String name,
                                                     @RequestBody @Valid RollerTransferDTO roller) {
        log.debug("Updating Roller: {}", name);

        return ResponseEntity.ok().body(rollerService.rollerTransfer(name, roller));
    }
}
