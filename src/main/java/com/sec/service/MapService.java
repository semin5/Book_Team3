package com.sec.service;

import com.sec.entity.Map;
import com.sec.repository.mongo.MapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {

    private final MapRepository mapRepository;

    public Map findByPostId(int postId) {
        return mapRepository.findByPostId(postId);
    }
    public void deleteByPostId(int postId) {
        mapRepository.deleteByPostId(postId);
    }
}
