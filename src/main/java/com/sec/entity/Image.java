package com.sec.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "image")
public @Data class Image {

    @Id
    private String id;
    private String filename;
    private String contentType;
    private long size;
    private LocalDateTime uploadDate;
    private Integer postId;
}
