package com.qtechgames.rollernetwork.controller;

import com.qtechgames.rollernetwork.dto.RollerDTO;
import com.qtechgames.rollernetwork.dto.RollerResponse;
import com.qtechgames.rollernetwork.dto.RollerTransferDTO;
import com.qtechgames.rollernetwork.service.IRollerService;
import com.qtechgames.rollernetwork.util.MapConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/rollernetwork")
@Tag(name = "Roller Management", description = "This Rest API provides the services to manage the online casino and its network of players.")
public class RollerController {

    private IRollerService rollerService;
    private MapConverter mapConverter;

    public RollerController(IRollerService rollerService, MapConverter mapConverter) {
        this.rollerService = rollerService;
        this.mapConverter = mapConverter;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/roller/{id}")
    public RollerResponse getRollerById(@PathVariable(value = "id") Long id) {
        log.debug("Getting the Roller by ID: {}", id);

        return mapConverter.convertToDto(rollerService.getRollerById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/roller")
    public RollerResponse getRollerByName(@RequestParam(value = "name") @Size(max = 50) String name) {
        log.debug("Getting the Roller: {}", name);

        return mapConverter.convertToDto(rollerService.getRollerByName(name));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/roller/{name}/downline")
    public List<String> getRollerDownline(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Getting downline of the Roller: {}", name);

        return rollerService.getRollerDownline(name);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/roller/{name}/referral")
    public RollerResponse getRollerReferral(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Getting the Roller referral of: {}", name);

        return mapConverter.convertToDto(rollerService.getRollerReferral(name));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/roller", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public RollerResponse createRoller(@RequestBody @Valid RollerDTO roller) {
        log.debug("Creating Roller: {}", roller.getName());

        return mapConverter.convertToDto(rollerService.createRoller(roller));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/roller/{name}/exit")
    public RollerResponse rollerExit(@PathVariable(value = "name") @Size(max = 50) String name) {
        log.debug("Roller leaving the network: {}", name);

        return mapConverter.convertToDto(rollerService.rollerExit(name));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/roller/{name}/transfer", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public RollerResponse rollerTransfer(@PathVariable(value = "name") @Size(max = 50) String name,
                                                     @RequestBody @Valid RollerTransferDTO referralRoller) {
        log.debug("Updating Roller: {}", name);

        return mapConverter.convertToDto(rollerService.rollerTransfer(name, referralRoller.getName()));
    }
}
