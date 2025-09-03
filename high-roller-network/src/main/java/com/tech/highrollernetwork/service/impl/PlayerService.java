package com.tech.highrollernetwork.service.impl;

import com.tech.highrollernetwork.dto.PlayerRequest;
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
import java.util.Objects;
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
     * Get the player's downline by the name
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
    public PlayerEntity createPlayer(final PlayerRequest playerRequest) {
        Optional<PlayerEntity> player = playerRepository.findByName(playerRequest.getName().toUpperCase());

        if (player.isPresent() && Boolean.FALSE.equals(player.get().getExit())) {
            throw new ResourceAlreadyExistsException("Player already exists on the network: " + playerRequest.getName());
        }
        if (player.isPresent()) {
            throw new ResourceAlreadyExistsException("Player already left the network: " + playerRequest.getName());
        }

        PlayerEntity referralPlayer = this.findPlayerByName(playerRequest.getParentName(), "Referral player not found: ");
        PlayerEntity playerEntity = PlayerEntity.builder()
                .name(playerRequest.getName().toUpperCase())
                .parentId(referralPlayer.getId())
                .referralChain(String.valueOf(referralPlayer.getId()))
                .exit(Boolean.FALSE)
                .build();

        return playerRepository.save(playerEntity);
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
        PlayerEntity player = this.findPlayerByName(playerName, "Player's name not found on the network: ");

        if (Objects.isNull(player.getReferralChain())) {
            throw new ResourceNotFoundException("The player does not have any referral: " + playerName);
        }

        Long referralId = Long.valueOf(player.getReferralChain().split(",")[0]);
        return this.findPlayerById(referralId, "Referral Player not found on the network: ");
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
        PlayerEntity player = this.findPlayerByName(playerName, "Player's name not found: ");
        if ("casino".equalsIgnoreCase(playerName) && (!player.getExit())) {
            allExit();
            return player;
        }

        player.setExit(Boolean.TRUE);
        updateReferrerOfPlayer(player);

        return playerRepository.save(player);
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
        PlayerEntity player = this.findPlayerByName(playerName, "Player's name not found: ");
        PlayerEntity referral = this.findPlayerByName(referralName, "New referrer not found: ");

        player.setParentId(referral.getId());
        player.setReferralChain(player.getReferralChain() + "," + referral.getId());

        return playerRepository.save(player);
    }

    /**
     * Update the player's referrer
     *
     * @param player {@link PlayerEntity}
     * @throws {@link ResourceNotFoundException}
     */
    private void updateReferrerOfPlayer(PlayerEntity player) {
        List<PlayerEntity> children = playerRepository.findByParentId(player.getId());

        if (!children.isEmpty()) {
            children.forEach(child -> {
                PlayerEntity newParent = findPlayerById(player.getParentId(), "Player's ID not found: ");
                playerTransfer(child.getName(), newParent.getName());
            });
        }
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
        Optional<PlayerEntity> player = playerRepository.findByName(playerName.toUpperCase());
        if (player.isEmpty() || Boolean.TRUE.equals(player.get().getExit())) {
            throw new ResourceNotFoundException(message + playerName);
        }

        return player.get();
    }

    /**
     * recursive method to get the downline list of a player
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
