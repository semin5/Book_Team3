package com.sec.service;

import com.sec.dto.CommentDto;
import com.sec.entity.Comment;
import com.sec.entity.Member;
import com.sec.entity.Post;
import com.sec.repository.CommentRepository;
import com.sec.repository.MemberRepository;
import com.sec.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public void createComment(CommentDto dto) {
        if (dto.getPostId() == null || dto.getMemberId() == null) {
            throw new IllegalArgumentException("postId 또는 memberId가 null입니다.");
        }

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPostId(Integer postId) {
        return commentRepository.findByPost_PostId(postId);
    }

    @Transactional
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updateComment(Integer commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        comment.setContent(content);
        commentRepository.save(comment);
    }
}
