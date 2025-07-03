package com.sec.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@ToString(exclude = {"posts", "comments", "reactions"})
public @Data class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private int memberId;

	@Column(nullable = false, unique = true, length = 30)
	private String nickname;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "member")
	private List<Post> posts = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "member")
	private List<Reaction> reactions = new ArrayList<>();
}
