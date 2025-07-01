package com.sec.mongo.repository;

import com.sec.entity.Map;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MapRepository extends MongoRepository<Map, String> {
    Map findByPostId(int postId);
}
