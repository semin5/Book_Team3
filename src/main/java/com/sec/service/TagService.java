package com.sec.service;

import com.sec.dto.TagDto;
import com.sec.repository.jpa.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public List<TagDto> findAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> new TagDto(tag.getTagId(), tag.getName()))
                .collect(Collectors.toList());
    }
}
