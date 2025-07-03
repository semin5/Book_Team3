package com.sec.controller;

import com.sec.dto.*;
import com.sec.entity.Comment;
import com.sec.entity.Image;
import com.sec.entity.Map;
import com.sec.security.CustomOAuth2User;
import com.sec.service.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final TagService tagService;
    private final CommentService commentService;
    private final ReactionService reactionService;
    private final MapService mapService;
    private final ImageService imageService;

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
    public String postDetail(@PathVariable int id, @SessionAttribute(value = "loginMember", required = false) Object loginMember, Model model, @AuthenticationPrincipal CustomOAuth2User principal) {

        PostResponse post = postService.getPost(id);
        Map map = mapService.findByPostId(id);
        if (map != null) {
            post.setMapInfo(MapRequest.mapRequest(map));
        }

        List<Comment> comments = commentService.getCommentsByPostId(id);
        int likeCount = reactionService.getReactionCount(id, TargetType.POST, ReactionType.LIKE);
        int dislikeCount = reactionService.getReactionCount(id, TargetType.POST, ReactionType.DISLIKE);
        List<Image> images = imageService.getImagesByPostId(post.getPostId());

        model.addAttribute("images", images);
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("likeCount", likeCount);
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
    public String createPost(@RequestParam java.util.Map<String, String> paramMap, @ModelAttribute @Valid PostCreateRequest request, BindingResult bindingResult, @AuthenticationPrincipal CustomOAuth2User principal, Model model, @RequestParam(value = "image", required = false) MultipartFile[] image) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("allTags", tagService.findAllTags());
            return "write";
        }

        int memberId = principal.getMember().getMemberId();
        int postid = postService.createPost(request, memberId);

        if (image != null) {
            for (MultipartFile images : image) {
                if (!images.isEmpty()) {
                    try {
                        imageService.storeImage(images, postid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "redirect:/posts/" + postid;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        PostResponse post = postService.getPost(id);

        model.addAttribute("post", post);
        model.addAttribute("allTags", tagService.findAllTags());

        List<Image> images = imageService.getImagesByPostId(id);
        if (!images.isEmpty()) {
            model.addAttribute("imageId", images.get(0).getId());
        }
        model.addAttribute("allTags", tagService.findAllTags());
        return "post_edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable int id, @ModelAttribute PostCreateRequest request, @AuthenticationPrincipal CustomOAuth2User principal, @RequestParam(value = "image", required = false) List<MultipartFile> newImage, @RequestParam(value = "deleteImage", required = false) String deleteImageFlag) throws IOException{
        int memberId = principal.getMember().getMemberId();
        postService.updatePost(id, request, memberId);

        boolean imageDeleted = false;

        if ("on".equals(deleteImageFlag)) {
            imageService.deleteImagesByPostId(id);
            imageDeleted = true;
        }

        if (newImage != null && !newImage.isEmpty()) {
            for (MultipartFile image : newImage) {
                if (!image.isEmpty()) {
                    imageService.storeImage(image, id);
                }
            }
        }

        return "redirect:/posts/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable int id, @AuthenticationPrincipal CustomOAuth2User principal) {
        int memberId = principal.getMember().getMemberId();
        postService.deletePost(id, memberId);
        mapService.deleteByPostId(id);
        imageService.deleteImagesByPostId(id);
        return "redirect:/posts";
    }
}