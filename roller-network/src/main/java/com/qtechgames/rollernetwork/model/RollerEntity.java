package com.qtechgames.rollernetwork.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Roller")
@Table(name = "roller")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RollerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @Column(name = "referral_chain", nullable = true)
    private String referralChain;

    @Column
    private Boolean exit;
}
