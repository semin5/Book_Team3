package com.sec.repository;

import com.sec.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
    List<Post> findByTagsNameContaining(String tagName);
    List<Post> findByContentContaining(String keyword);
    @EntityGraph(attributePaths = {"tags", "member"})
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

}
