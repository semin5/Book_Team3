package com.sec.repository;

import com.sec.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByMember_MemberIdAndTargetTypeAndTargetId(int memberId, String targetType, int targetId);
    int countByTargetTypeAndTargetIdAndReactionType(String targetType, int targetId, String reactionType);
    void deleteByMember_MemberIdAndTargetTypeAndTargetId(int memberId, String targetType, int targetId);
}
