package com.sec.entity;

import java.time.LocalDateTime;

import com.sec.dto.ReactionType;
import com.sec.dto.TargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints =
    @UniqueConstraint(columnNames = {"member_id", "target_type", "target_id"}))
public @Data class Reaction {

    @Id
    @Column(name = "reaction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reactionId;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name= "target_type", nullable = false,
            columnDefinition = "ENUM('POST', 'COMMENT')")
    private TargetType targetType;

    @Column(name="target_id", nullable = false)
    private int targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false,
            columnDefinition = "ENUM('LIKE', 'DISLIKE')")
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
}