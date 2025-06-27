package com.sec;

import com.sec.entity.Member;
import com.sec.entity.Post;
import com.sec.repository.MemberRepository;
import com.sec.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
@Rollback(false)
public class SpringBootLab09ApplicationTests {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void insertDummyPosts() {
		String uniqueNickname = "테스터" + UUID.randomUUID().toString().substring(0, 6);
		Member member = memberRepository.save(Member.builder()
				.nickname(uniqueNickname)
				.email("test@example.com")
				.build());

		for (int i = 1; i <= 100; i++) {
			postRepository.save(Post.builder()
					.title("테스트 제목 " + i)
					.content("테스트 내용 " + i)
					.member(member)
					.build());
		}
	}
}
