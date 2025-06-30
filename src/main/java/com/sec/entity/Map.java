package com.sec.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "map")
public @Data class Map {
    @Id
    private String id;
    private int postId;
    private String address;
    private double latitude;
    private double longitude;
}
