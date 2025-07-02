package com.sec.service;

import com.sec.dto.ReactionType;
import com.sec.dto.TargetType;
import com.sec.entity.Reaction;
import com.sec.repository.jpa.MemberRepository;
import com.sec.repository.jpa.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReactionService {

    private final MemberRepository memberRepository;
    private final ReactionRepository reactionRepository;
    private final MemberService memberService;

    @Transactional
    public int react(int targetId, int memberId, TargetType targetType, String reactionType) {
        ReactionType type = ReactionType.valueOf(reactionType.toUpperCase());
        Reaction existing = reactionRepository.findByMember_MemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId).orElse(null);

        if (existing != null) {
            if (existing.getReactionType() == type) {
                reactionRepository.delete(existing);
                return -1;
            } else {
                existing.setReactionType(type);
                existing.setCreatedAt(LocalDateTime.now());
                reactionRepository.save(existing);
                return 2;
            }
        }

        Reaction newReaction = new Reaction();
        newReaction.setMember(memberService.getMemberById(memberId));
        newReaction.setTargetType(targetType);
        newReaction.setReactionType(type);
        newReaction.setTargetId(targetId);
        newReaction.setCreatedAt(LocalDateTime.now());

        reactionRepository.save(newReaction);
        return 1;
    }

    public int getReactionCount(int targetId, TargetType type, ReactionType reactionType) {
        return reactionRepository.countByTargetIdAndTargetTypeAndReactionType(targetId, type, reactionType);
    }

    public int getTotalReactionCount(int targetId, TargetType type) {
        int likes = getReactionCount(targetId, type, ReactionType.LIKE);
        int dislikes = getReactionCount(targetId, type, ReactionType.DISLIKE);
        return likes - dislikes;
    }
}