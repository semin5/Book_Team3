package com.sec.repository.jpa;

import com.sec.dto.ReactionType;
import com.sec.dto.TargetType;
import com.sec.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByMember_MemberIdAndTargetTypeAndTargetId(int memberId, TargetType targetType, int targetId);
    int countByTargetIdAndTargetTypeAndReactionType(int targetId, TargetType targetType, ReactionType reactionType);
}
