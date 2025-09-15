package com.tech.highrollernetworkreactive.service.impl;

import com.tech.highrollernetworkreactive.dto.PlayerRequest;
import com.tech.highrollernetworkreactive.exceptionhandler.ResourceAlreadyExistsException;
import com.tech.highrollernetworkreactive.exceptionhandler.ResourceNotFoundException;
import com.tech.highrollernetworkreactive.model.PlayerEntity;
import com.tech.highrollernetworkreactive.repository.PlayerRepository;
import com.tech.highrollernetworkreactive.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(@Lazy PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public Mono<PlayerEntity> getPlayerById(final Long playerId) {
        return this.findPlayerById(playerId, "Player ID not found: ");
    }

    @Override
    public Mono<PlayerEntity> getPlayerByName(final String playerName) {
        return playerRepository.findByName(playerName.toUpperCase())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Player's name not found: " + playerName)));
    }

    @Override
    public Mono<PlayerEntity> createPlayer(final PlayerRequest playerRequest) {
        return playerRepository.findByName(playerRequest.getName().toUpperCase())
                .flatMap(player -> {
                    if (Boolean.FALSE.equals(player.getExit())) {
                        return Mono.error(new ResourceAlreadyExistsException("Player already exists on the network: " + playerRequest.getName()));
                    } else {
                        player.setExit(Boolean.FALSE);
                        return playerRepository.save(player);
                    }
                })
                .switchIfEmpty(addNewPlayer(playerRequest));
    }

    @Override
    public Mono<PlayerEntity> getPlayerReferrer(final String playerName) {
        return this.findPlayerByName(playerName, "Player's name not found on the network: ")
                .filter(player -> Objects.nonNull(player.getReferralChain()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("The player " + playerName + " does not have any referral.")))
                .map(referral -> Long.valueOf(referral.getReferralChain().substring(0,1)))
                .flatMap(referralId -> this.findPlayerById(referralId, "Referral player not found on the network: "));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Mono<PlayerEntity> playerExit(final String playerName) {
        if ("casino".equalsIgnoreCase(playerName)) {
            return casinoToExit(playerName);
        }

        return findPlayerByName(playerName, "Player's name not found: ")
                .flatMap(player -> {
                    player.setExit(Boolean.TRUE);
                    return playerRepository.save(player);
                })
                .flatMap(player -> playerRepository.findByParentId(player.getId())
                        .map(child -> {
                            child.setParentId(player.getParentId());
                            child.setReferralChain(child.getReferralChain() + "," + player.getParentId());
                            return child;
                        })
                        .flatMap(playerRepository::save)
                        .then(Mono.just(player)));
    }

    @Override
    public Mono<PlayerEntity> playerTransfer(final String playerName, final String newReferral) {
        Mono<PlayerEntity> player = this.findPlayerByName(playerName, "Player's name not found: ");
        Mono<PlayerEntity> referralPlayer = this.findPlayerByName(newReferral, "New referrer not found: ");

        return Mono.zip(player, referralPlayer).map(tuple -> {
            PlayerEntity playerEntity = tuple.getT1();
            PlayerEntity referral = tuple.getT2();

            playerEntity.setParentId(referral.getId());
            playerEntity.setReferralChain(playerEntity.getReferralChain() + "," + referral.getId());

            return playerEntity;
        }).flatMap(playerRepository::save);
    }

    private Mono<PlayerEntity> addNewPlayer(PlayerRequest playerRequest) {
        return this.findPlayerByName(playerRequest.getParentName(), "Referral player not found: ")
                .flatMap(referralPlayer -> {
                    PlayerEntity playerEntity = PlayerEntity.builder()
                            .name(playerRequest.getName().toUpperCase())
                            .parentId(referralPlayer.getId())
                            .referralChain(String.valueOf(referralPlayer.getId()))
                            .exit(Boolean.FALSE)
                            .build();
                    return playerRepository.save(playerEntity);
                });
    }

    private Mono<PlayerEntity> casinoToExit(String playerName) {
        return playerRepository.findAll()
                .map(player -> {
                    player.setExit(Boolean.TRUE);
                    return player;
                })
                .flatMap(playerRepository::save)
                .then(playerRepository.findByName(playerName));
    }

    private Mono<PlayerEntity> findPlayerById(Long playerId, String message) {
        return playerRepository.findById(playerId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(message + playerId)));
    }

    private Mono<PlayerEntity> findPlayerByName(String playerName, String message) {
        return playerRepository.findByName(playerName.toUpperCase())
                .filter(player -> Boolean.FALSE.equals(player.getExit()))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(message + playerName)));
    }

}
