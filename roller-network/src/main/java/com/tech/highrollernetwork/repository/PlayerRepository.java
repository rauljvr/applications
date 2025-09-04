package com.tech.highrollernetwork.repository;

import com.tech.highrollernetwork.model.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, Long> {

    public Optional<PlayerEntity> findByName(String playerName);

    public List<PlayerEntity> findByParentId(Long parentId);
}
