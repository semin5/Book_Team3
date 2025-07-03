package com.sec.controller;

import com.sec.dto.PostResponse;
import com.sec.dto.PostSearchCondition;
import com.sec.entity.Map;
import com.sec.entity.Post;
import com.sec.service.MapService;
import com.sec.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final PostService postService;

    @GetMapping
    public String showMap(@ModelAttribute("condition") PostSearchCondition condition, @RequestParam(value = "sort", defaultValue = "createdAt") String sortType, Model model) {
        List<PostResponse> postList = postService.getPostsByCondition(condition, Pageable.unpaged(), sortType).getContent();

        List<java.util.Map<String, Object>> locations = postList.stream()
                .map(post  -> {
                    try {
                        Map map = mapService.findByPostId(post.getPostId());
                        if (map == null) return null;

                        java.util.Map<String, Object> location = new HashMap<>();
                        location.put("address", map.getAddress());
                        location.put("latitude", map.getLatitude());
                        location.put("longitude", map.getLongitude());
                        location.put("title", post.getTitle());
                        location.put("isSolved", post.isSolved());
                        location.put("postId", post.getPostId());

                        if (post.isSolved() && post.getBookTitle() != null) {
                            location.put("bookTitle", post.getBookTitle());
                            location.put("bookAuthor", post.getBookAuthor());
                        }

                        return location;
                    }catch (IllegalArgumentException e) {
                            // 존재하지 않는 게시글은 무시
                            return null;
                    }
                })
                .filter(loc -> loc != null)
                .toList();

        model.addAttribute("condition", condition);
        model.addAttribute("locations", locations);
        return "map";
    }
}
