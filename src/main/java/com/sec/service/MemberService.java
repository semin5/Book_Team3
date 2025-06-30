package com.sec.service;

import com.sec.entity.Member;
import com.sec.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 ID로 조회 (없으면 예외)
    public Member getMemberById(int id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + id));
    }

    // 이메일로 조회 (선택)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    // 신규 저장
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    // 삭제
    public void deleteById(int id) {
        memberRepository.deleteById(id);
    }
}