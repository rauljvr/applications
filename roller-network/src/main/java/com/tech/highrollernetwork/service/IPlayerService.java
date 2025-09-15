package com.tech.highrollernetwork.service;

import com.tech.highrollernetwork.dto.PlayerRequest;
import com.tech.highrollernetwork.model.PlayerEntity;

import java.util.List;

public interface IPlayerService {

    public PlayerEntity getPlayerById(final Long playerId);

    public PlayerEntity getPlayerByName(final String playerName);

    public List<String> getPlayerDownline(final String playerName);

    public PlayerEntity createPlayer(final PlayerRequest player);

    public PlayerEntity getPlayerReferrer(final String playerName);

    public PlayerEntity playerExit(final String playerName);

    public PlayerEntity playerTransfer(final String playerName, final String referralName);
}
