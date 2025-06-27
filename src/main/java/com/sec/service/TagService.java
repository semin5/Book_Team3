package com.sec.service;

import com.sec.dto.TagDto;
import com.sec.entity.Tag;
import com.sec.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    public void deleteById(int id) {
        tagRepository.deleteById(id);
    }
}
