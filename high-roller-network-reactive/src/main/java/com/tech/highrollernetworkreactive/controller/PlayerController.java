package com.tech.highrollernetworkreactive.controller;

import com.tech.highrollernetworkreactive.dto.PlayerRequest;
import com.tech.highrollernetworkreactive.dto.PlayerResponse;
import com.tech.highrollernetworkreactive.dto.PlayerTransferRequest;
import com.tech.highrollernetworkreactive.service.IPlayerService;
import com.tech.highrollernetworkreactive.util.MapConverter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
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
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/highrollernetworkreactive")
@Tag(name = "High Roller's Network Management", description = "This Rest API provides the services to manage the online casino and its network of players.")
public class PlayerController {

    private IPlayerService playerService;
    private MapConverter mapConverter;

    public PlayerController(@Lazy IPlayerService playerService, MapConverter mapConverter) {
        this.playerService = playerService;
        this.mapConverter = mapConverter;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player/{id}")
    public Mono<PlayerResponse> getPlayerById(@PathVariable(value = "id") Long playerId) {
        log.info("Getting the Player by ID: {}", playerId);

        return playerService.getPlayerById(playerId)
                .map(player -> mapConverter.convertToDto(player));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player")
    public Mono<PlayerResponse> getPlayerByName(@RequestParam(value = "name") @Size(max = 20) String name) {
        log.info("Getting the Player: {}", name);

        return playerService.getPlayerByName(name)
                .map(player -> mapConverter.convertToDto(player));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/player/{name}/referral")
    public Mono<PlayerResponse> getPlayerReferral(@PathVariable(value = "name") @Size(max = 20) String playerName) {
        log.info("Getting the Player referral of: {}", playerName);

        return playerService.getPlayerReferrer(playerName)
                .map(player -> mapConverter.convertToDto(player));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/player")
    public Mono<PlayerResponse> createPlayer(@RequestBody @Valid PlayerRequest playerRequest) {
        log.info("Creating Player: {}", playerRequest.getName());

        return playerService.createPlayer(playerRequest)
                .map(player -> mapConverter.convertToDto(player));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/player/{name}/exit")
    public Mono<PlayerResponse> playerExit(@PathVariable(value = "name") @Size(max = 20) String playerName) {
        log.info("Player leaving the network: {}", playerName);

        return playerService.playerExit(playerName)
                .map(player -> mapConverter.convertToDto(player));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/player/{name}/transfer")
    public Mono<PlayerResponse> playerTransfer(@PathVariable(value = "name") @Size(max = 20) String playerName,
                                             @RequestBody @Valid PlayerTransferRequest transferRequest) {
        log.info("Updating Player: {}", playerName);

        return playerService.playerTransfer(playerName, transferRequest.getParentName())
                .map(player -> mapConverter.convertToDto(player));
    }
}
