package com.sec.controller;

import com.sec.dto.PostResponse;
import com.sec.dto.PostSearchCondition;
import com.sec.entity.Member;
import com.sec.repository.jpa.MemberRepository;
import com.sec.security.CustomOAuth2User;
import com.sec.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MemberController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    @GetMapping
    public String myPages(Model model, @AuthenticationPrincipal CustomOAuth2User principal) {
        Member member = principal.getMember();
        int memberId = member.getMemberId();

        long postCount = memberRepository.countPostsByMemberId(memberId);
        long commentCount = memberRepository.countCommentsByMemberId(memberId);
        long likeCount = memberRepository.countLikesByMemberId(memberId);
        long dislikeCount = memberRepository.countDislikesByMemberId(memberId);

        model.addAttribute("member", member);
        model.addAttribute("postCount", postCount);
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("dislikeCount", dislikeCount);
        return "mypage";
    }

    @GetMapping("/posts")
    public String myWrittenPosts(Model model, @AuthenticationPrincipal CustomOAuth2User principal, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword, @RequestParam(required = false) Boolean isSolved, @RequestParam(required = false, defaultValue = "createdAt") String sort, @RequestParam(required = false) String tag) {
        int memberId = principal.getMember().getMemberId();
        PostSearchCondition condition = new PostSearchCondition();
        condition.setKeyword(keyword);
        condition.setIsSolved(isSolved);
        condition.setTag(tag);

        Pageable myPostPageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<PostResponse> myPosts = postService.getPostsWrittenByMember(memberId, condition, myPostPageable, sort);

        model.addAttribute("myPosts", myPosts);
        model.addAttribute("condition", condition);
        model.addAttribute("sort", sort);
        return "mypage_posts";
    }

    @GetMapping("/likes")
    public String myLikedPosts(Model model, @AuthenticationPrincipal CustomOAuth2User principal, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false) String keyword, @RequestParam(required = false) Boolean isSolved, @RequestParam(required = false, defaultValue = "createdAt") String sort, @RequestParam(required = false) String tag) {
        int memberId = principal.getMember().getMemberId();
        PostSearchCondition condition = new PostSearchCondition();
        condition.setKeyword(keyword);
        condition.setIsSolved(isSolved);
        condition.setTag(tag);

        Pageable likedPageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<PostResponse> likedPosts = postService.getPostsLikedByMember(memberId, condition, likedPageable, sort);

        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("condition", condition);
        model.addAttribute("sort", sort);
        return "mypage_likes";
    }
}
