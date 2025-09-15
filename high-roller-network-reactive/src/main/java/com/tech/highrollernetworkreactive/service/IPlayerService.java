package com.tech.highrollernetworkreactive.service;

import com.tech.highrollernetworkreactive.dto.PlayerRequest;
import com.tech.highrollernetworkreactive.model.PlayerEntity;
import reactor.core.publisher.Mono;

public interface IPlayerService {

    public Mono<PlayerEntity> getPlayerById(final Long playerId);

    public Mono<PlayerEntity> getPlayerByName(final String playerName);

    public Mono<PlayerEntity> createPlayer(final PlayerRequest player);

    public Mono<PlayerEntity> getPlayerReferrer(final String playerName);

    public Mono<PlayerEntity> playerExit(final String playerName);

    public Mono<PlayerEntity> playerTransfer(final String playerName, final String newReferral);
}
