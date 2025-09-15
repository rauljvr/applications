package com.tech.highrollernetworkreactive.repository;

import com.tech.highrollernetworkreactive.model.PlayerEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends R2dbcRepository<PlayerEntity, Long> {

    public Mono<PlayerEntity> findByName(String playerName);

    public Flux<PlayerEntity> findByParentId(Long parentId);
}
