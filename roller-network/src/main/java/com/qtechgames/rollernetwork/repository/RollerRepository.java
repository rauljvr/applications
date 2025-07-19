package com.qtechgames.rollernetwork.repository;

import com.qtechgames.rollernetwork.model.RollerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RollerRepository extends JpaRepository<RollerEntity, Long> {

    public Optional<RollerEntity> findByName(String name);

    public List<RollerEntity> findByParentId(Long parentId);
}
