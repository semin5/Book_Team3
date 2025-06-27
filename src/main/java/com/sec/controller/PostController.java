package com.sec.controller;

import com.sec.dto.PostCreateRequest;
import com.sec.dto.PostResponse;
import com.sec.dto.PostSearchCondition;
import com.sec.dto.TagDto;
import com.sec.entity.Comment;
import com.sec.entity.Tag;
import com.sec.security.CustomOAuth2User;
import com.sec.service.CommentService;
import com.sec.service.PostService;
import com.sec.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private  final CommentService commentService;

    @GetMapping
    public String listPosts(@ModelAttribute("condition") PostSearchCondition condition, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<PostResponse> posts = postService.getPostsByCondition(condition, pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("condition", condition);
        return "list";
    }

    @GetMapping("/{id}")
    public String postDetail(@PathVariable int id, @SessionAttribute(value = "loginMember", required = false) Object loginMember,  Model model) {
        PostResponse post = postService.getPost(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        return "post_detail";
    }

    @GetMapping("/write")
    public String writeForm(Model model, @AuthenticationPrincipal CustomOAuth2User principal) {
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        model.addAttribute("allTags", tagService.findAllTags());
        model.addAttribute("currentUser", principal.getMember());
        return "write";
    }

    @PostMapping("/write")
    public String createPost(@ModelAttribute @Valid PostCreateRequest request, BindingResult bindingResult, @AuthenticationPrincipal CustomOAuth2User principal, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allTags", tagService.findAllTags());
            return "write";
        }

        int memberId = principal.getMember().getMemberId();
        postService.createPost(request, memberId);
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        PostResponse post = postService.getPost(id);

        model.addAttribute("post", post);
        model.addAttribute("allTags", tagService.findAllTags());
        return "post_edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable int id, @ModelAttribute PostCreateRequest request, @AuthenticationPrincipal CustomOAuth2User principal) {
        int memberId = principal.getMember().getMemberId();
        postService.updatePost(id, request, memberId);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable int id, @AuthenticationPrincipal CustomOAuth2User principal) {
        int memberId = principal.getMember().getMemberId();
        postService.deletePost(id, memberId);
        return "redirect:/posts";
    }
}