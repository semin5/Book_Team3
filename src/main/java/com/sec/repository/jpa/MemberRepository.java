package com.sec.repository.jpa;

import com.sec.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByEmail(String email);
    @Query("SELECT COUNT(p) FROM Post p WHERE p.member.memberId = :memberId")
    long countPostsByMemberId(@Param("memberId") int memberId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.member.memberId = :memberId")
    long countCommentsByMemberId(@Param("memberId") int memberId);

    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.member.memberId = :memberId AND r.reactionType = 'LIKE'")
    long countLikesByMemberId(@Param("memberId") int memberId);

    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.member.memberId = :memberId AND r.reactionType = 'DISLIKE'")
    long countDislikesByMemberId(@Param("memberId") int memberId);
}
