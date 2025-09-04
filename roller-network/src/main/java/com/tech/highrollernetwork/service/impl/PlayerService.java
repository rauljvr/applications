package com.tech.highrollernetwork.service.impl;

import com.tech.highrollernetwork.dto.PlayerRequest;
import com.tech.highrollernetwork.exceptionhandler.GenericException;
import com.tech.highrollernetwork.exceptionhandler.ResourceAlreadyExistsException;
import com.tech.highrollernetwork.exceptionhandler.ResourceNotFoundException;
import com.tech.highrollernetwork.model.PlayerEntity;
import com.tech.highrollernetwork.repository.PlayerRepository;
import com.tech.highrollernetwork.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PlayerService implements IPlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(@Lazy PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Get the player's info by the ID
     *
     * @param playerId
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    @Override
    public PlayerEntity getPlayerById(final Long playerId) {
        return this.findPlayerById(playerId, "Player's ID not found: ");
    }

    /**
     * Get the player's info by the name
     *
     * @param playerName
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    @Override
    public PlayerEntity getPlayerByName(final String playerName) {
        return playerRepository.findByName(playerName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Player's name not found: " + playerName));
    }

    /**
     * Get the player's downline recursively
     *
     * @param playerName
     * @return list of player's names
     * @throws {@link ResourceNotFoundException}
     */
    @Override
    public List<String> getPlayerDownline(final String playerName) {
        PlayerEntity player = this.findPlayerByName(playerName, "Player's name not found on the network: ");

        List<String> downlineList = new ArrayList<>();
        downline(downlineList, player);

        return downlineList;
    }

    /**
     * Create a new player on the network
     *
     * @param {@link PlayerRequest}
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}, {@link ResourceAlreadyExistsException}
     */
    @Override
    public PlayerEntity createPlayer(final PlayerRequest player) {
        playerRepository.findByName(player.getName().toUpperCase())
                .ifPresent(p -> {
                    if (Boolean.FALSE.equals(p.getExit())) {
                        throw new ResourceAlreadyExistsException("Player already exists on the network: "
                                + player.getName());
                    } else {
                        throw new ResourceNotFoundException("Player already left the network: " + player.getName());
                    }
                });

        return Optional.of(this.findPlayerByName(player.getParentName(), "Referral player not found on the network: "))
                .map(referralPlayer -> PlayerEntity.builder()
                        .name(player.getName().toUpperCase())
                        .parentId(referralPlayer.getId())
                        .referralChain(String.valueOf(referralPlayer.getId()))
                        .exit(Boolean.FALSE)
                        .build())
                .map(playerRepository::save)
                .orElseThrow(() -> new GenericException("Unexpected error creating player."));
    }

    /**
     * Get the referrer of the given player
     *
     * @param playerName
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    @Override
    public PlayerEntity getPlayerReferrer(final String playerName) {
        return Optional.of(this.findPlayerByName(playerName, "Player's name not found on the network: "))
                .map(PlayerEntity::getReferralChain)
                .map(chain -> chain.split(",")[0])
                .map(Long::valueOf)
                .map(referralId -> this.findPlayerById(referralId, "Referral Player not found on the network: "))
                .orElseThrow(() -> new ResourceNotFoundException("The player does not have any referral: " + playerName));
    }

    /**
     * Handle when a player leaves the network by changing the exit flag to true
     *
     * @param playerName
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public PlayerEntity playerExit(final String playerName) {
        return Optional.of(this.findPlayerByName(playerName, "Player's name not found: "))
                .map(player -> {
                    if ("casino".equalsIgnoreCase(playerName) && Boolean.FALSE.equals(player.getExit())) {
                        allExit();
                        return player;
                    }
                    player.setExit(Boolean.TRUE);
                    updateReferrerOfPlayer(player);
                    return playerRepository.save(player);
                })
                .orElseThrow(() -> new GenericException("Unexpected error during player exit"));
    }

    /**
     * Handle when a player is transfered on the network by updating the referrer and the referral chain
     *
     * @param playerName
     * @param referralName
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    @Override
    public PlayerEntity playerTransfer(final String playerName, final String referralName) {
        return Optional.of(this.findPlayerByName(playerName, "Player's name not found: "))
                .flatMap(player -> Optional.of(this.findPlayerByName(referralName, "New referrer not found: "))
                        .map(referral -> {
                            player.setParentId(referral.getId());
                            player.setReferralChain(player.getReferralChain() + "," + referral.getId());
                            return player;
                        }))
                .map(playerRepository::save)
                .orElseThrow(() -> new GenericException("Unexpected error during player transfer"));
    }

    /**
     * Update the player's referrer
     *
     * @param player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    private void updateReferrerOfPlayer(PlayerEntity player) {
        playerRepository.findByParentId(player.getId())
                .forEach(child -> {
                    PlayerEntity newParent = findPlayerById(player.getParentId(), "Player's ID not found: ");
                    playerTransfer(child.getName(), newParent.getName());
                });
    }

    /**
     * Handle when the casino leaves the network which means all players have to leave the network
     */
    private void allExit() {
        playerRepository.findAll().forEach(player -> {
            player.setExit(Boolean.TRUE);
            playerRepository.save(player);
        });
    }

    /**
     * Get a player by the ID with a custom exception message to be thrown if the player does not exist
     *
     * @param playerId
     * @param message
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    private PlayerEntity findPlayerById(Long playerId, String message) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException(message + playerId));
    }

    /**
     * Get a player by the name with a custom exception message to be thrown if the player does not exist
     *
     * @param playerName
     * @param message
     * @return player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    private PlayerEntity findPlayerByName(String playerName, String message) {
        return playerRepository.findByName(playerName.toUpperCase())
                .filter(p -> Boolean.FALSE.equals(p.getExit()))
                .orElseThrow(() -> new ResourceNotFoundException(message + playerName));
    }

    /**
     * recursive method to get the player downline list of a player
     *
     * @param downlineList to list the player's downline
     * @param player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    private void downline(List<String> downlineList, PlayerEntity player) {
        if (Boolean.FALSE.equals(player.getExit())) {
            downlineList.add(player.getName());
        }

        List<PlayerEntity> children = playerRepository.findByParentId(player.getId());
        if (children.isEmpty()) {
            return;
        }

        children.forEach(child -> downline(downlineList, child));
    }

}
