package com.sec.repository;

import com.sec.dto.ReactionType;
import com.sec.dto.TargetType;
import com.sec.entity.Member;
import com.sec.entity.Post;
import com.sec.entity.Reaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    Optional<Reaction> findByMember_MemberIdAndTargetTypeAndTargetId(int memberId, TargetType targetType, int targetId);
    int countByTargetIdAndTargetTypeAndReactionType(int targetId, TargetType targetType, ReactionType reactionType);
}
