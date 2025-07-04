package com.sec.repository.jpa;

import com.sec.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
    @EntityGraph(attributePaths = {"tags", "member"})
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);
    @Query("SELECT p.member.memberId FROM Post p WHERE p.postId = :postId")
    Optional<Integer> findWriterIdByPostId(@Param("postId") int postId);
}
