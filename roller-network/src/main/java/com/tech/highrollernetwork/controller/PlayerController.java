package com.tech.highrollernetwork.controller;

import com.tech.highrollernetwork.dto.PlayerRequest;
import com.tech.highrollernetwork.dto.PlayerResponse;
import com.tech.highrollernetwork.dto.PlayerTransferRequest;
import com.tech.highrollernetwork.service.IPlayerService;
import com.tech.highrollernetwork.util.MapConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
@RequestMapping(path = "/highrollernetwork")
@Tag(name = "High Roller's Network Management", description = "This Rest API provides the services to manage the online casino and its network of players.")
public class PlayerController {

    private IPlayerService playerService;
    private MapConverter mapConverter;

    public PlayerController(IPlayerService playerService, MapConverter mapConverter) {
        this.playerService = playerService;
        this.mapConverter = mapConverter;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player/{id}")
    public PlayerResponse getPlayerById(@PathVariable(value = "id") Long playerId) {
        log.info("Getting the Player by ID: {}", playerId);

        return mapConverter.convertToDto(playerService.getPlayerById(playerId));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player")
    public PlayerResponse getPlayerByName(@RequestParam(value = "name") @Size(max = 20) String playerName) {
        log.info("Getting the Player: {}", playerName);

        return mapConverter.convertToDto(playerService.getPlayerByName(playerName));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player/{name}/downline")
    public List<String> getPlayerDownline(@PathVariable(value = "name") @Size(max = 20) String playerName) {
        log.info("Getting downline of the Player: {}", playerName);

        return playerService.getPlayerDownline(playerName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player/{name}/referrer")
    public PlayerResponse getPlayerReferrer(@PathVariable(value = "name") @Size(max = 20) String playerName) {
        log.info("Getting the Player's referrer of: {}", playerName);

        return mapConverter.convertToDto(playerService.getPlayerReferrer(playerName));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/player")
    public PlayerResponse createPlayer(@RequestBody @Valid PlayerRequest player) {
        log.info("Creating Player: {}", player.getName());

        return mapConverter.convertToDto(playerService.createPlayer(player));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/player/{name}/exit")
    public PlayerResponse playerExit(@PathVariable(value = "name") @Size(max = 20) String playerName) {
        log.info("Player leaving the network: {}", playerName);

        return mapConverter.convertToDto(playerService.playerExit(playerName));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/player/{name}/transfer")
    public PlayerResponse playerTransfer(@PathVariable(value = "name") @Size(max = 20) String playerName,
                                         @RequestBody @Valid PlayerTransferRequest referralPlayer) {
        log.info("Updating Player: {}", playerName);

        return mapConverter.convertToDto(playerService.playerTransfer(playerName, referralPlayer.getName()));
    }
}
