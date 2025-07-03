package com.sec.service;

import com.sec.dto.CommentDto;
import com.sec.entity.Book;
import com.sec.entity.Comment;
import com.sec.entity.Member;
import com.sec.entity.Post;
import com.sec.repository.jpa.BookRepository;
import com.sec.repository.jpa.CommentRepository;
import com.sec.repository.jpa.MemberRepository;
import com.sec.repository.jpa.PostRepository;
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
    private final BookRepository bookRepository;

    public void createComment(CommentDto dto) {
        if (dto.getPostId() == null || dto.getMemberId() == null) {
            throw new IllegalArgumentException("postId 또는 memberId가 null입니다.");
        }

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Book book = null;
        if (dto.getBookTitle() != null && dto.getBookAuthor() != null) {
            book = bookRepository.findByTitleAndAuthor(dto.getBookTitle(), dto.getBookAuthor())
                    .orElseGet(() -> {
                        Book newBook = Book.builder()
                                .title(dto.getBookTitle())
                                .author(dto.getBookAuthor())
                                .build();
                        return bookRepository.save(newBook);
                    });
        }

        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(dto.getContent())
                .book(book)
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
    public void updateComment(Integer commentId, String content, String bookTitle, String bookAuthor, Integer bookId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        comment.setContent(content);

        if (bookId != null) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new IllegalArgumentException("기존 책이 존재하지 않습니다."));
            book.setTitle(bookTitle.trim());
            book.setAuthor(bookAuthor.trim());
            bookRepository.save(book);
            comment.setBook(book);
        } else {
            Book existingBook = bookRepository.findByTitleAndAuthor(bookTitle.trim(), bookAuthor.trim())
                    .orElse(null);

            if (existingBook != null) {
                comment.setBook(existingBook);
            } else {
                Book newBook = Book.builder()
                        .title(bookTitle.trim())
                        .author(bookAuthor.trim())
                        .build();
                bookRepository.save(newBook);
                comment.setBook(newBook);
            }
        }

        commentRepository.save(comment);
    }

    @Transactional
    public void adoptComment(int commentId, int postId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getIsSolved()) {
            post.setIsSolved(true);
            postRepository.save(post);

            comment.setIs_selected(true);
            commentRepository.save(comment);
        } else {
            throw new RuntimeException("이미 채택된 게시글입니다.");

        }
    }
}
