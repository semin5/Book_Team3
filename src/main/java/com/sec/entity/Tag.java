package com.sec.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "posts")
@EqualsAndHashCode(exclude = "posts")
public @Data class Tag {
	
	@Id
	@Column(name = "tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int tagId;
	
	@Column(length = 20, nullable = false, unique = true)
	private String name;
	
	@ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();
}
