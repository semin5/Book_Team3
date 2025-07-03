package com.sec.repository.mongo;

import com.sec.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByPostId(Integer postId);
}
