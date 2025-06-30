package com.sec.controller;

import com.sec.dto.*;
import com.sec.entity.Comment;
import com.sec.entity.Tag;
import com.sec.security.CustomOAuth2User;
import com.sec.service.CommentService;
import com.sec.service.PostService;
import com.sec.service.ReactionService;
import com.sec.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ReactionService reactionService;

    @GetMapping
    public String listPosts(@ModelAttribute("condition") PostSearchCondition condition, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, Model model,  @RequestParam(value = "sort", defaultValue = "createdAt") String sortType) {

        Pageable safePageable;
        if ("like".equalsIgnoreCase(sortType) || "dislike".equalsIgnoreCase(sortType)) {
            safePageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        } else {
            safePageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdAt")
            );
        }

        Page<PostResponse> posts = postService.getPostsByCondition(condition, safePageable, sortType);

        posts.getContent().forEach(post ->
                post.setTotalReactionCount(
                        reactionService.getTotalReactionCount(post.getPostId(), TargetType.POST)
                )
        );

        model.addAttribute("posts", posts);
        model.addAttribute("condition", condition);
        model.addAttribute("sort", sortType);
        return "list";
    }

    @GetMapping("/{id}")
    public String postDetail(@PathVariable int id, @SessionAttribute(value = "loginMember", required = false) Object loginMember,  Model model, @AuthenticationPrincipal CustomOAuth2User principal) {
        PostResponse post = postService.getPost(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("post", post);

        int likeCount = reactionService.getReactionCount(id, TargetType.POST, ReactionType.LIKE);
        model.addAttribute("likeCount", likeCount);

        int dislikeCount = reactionService.getReactionCount(id, TargetType.POST, ReactionType.DISLIKE);
        model.addAttribute("dislikeCount", dislikeCount);

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