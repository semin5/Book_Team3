package com.sec.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"tags", "comments"})
public @Data class Post {
	
	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int postId;
	
	@ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
	
	@Column(nullable = false)
	private String title;

	@Lob
	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;
	
	private int view_cnt = 0;
	
	@Column(name = "is_solved")
	private Boolean isSolved = false;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Comment> comments = new ArrayList<>();
}
