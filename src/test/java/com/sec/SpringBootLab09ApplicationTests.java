package com.sec;

import com.mongodb.client.MongoClients;
import com.sec.repository.mongo.MapRepository;
import com.sec.repository.jpa.MemberRepository;
import com.sec.repository.jpa.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(false)
public class SpringBootLab09ApplicationTests {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MapRepository mapRepository;

	/*
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

	@Test
	void testMongoSave() {
		MapInfo map = MapInfo.builder()
				.postId(99)
				.address("서울 중구 세종대로 110")
				.latitude(37.5665)
				.longitude(126.9780)
				.build();

		mapRepository.save(map); // ← 반드시 호출해야 저장됨

		// 저장된 결과 확인 로그 (선택)
		MapInfo saved = mapRepository.findByPostId(99);
		System.out.println("저장 확인: " + saved);
	}
	*/
	@Test
	void checkMongoConnection() {
		var client = MongoClients.create("mongodb://localhost:27017");
		var db = client.getDatabase("team3");
		System.out.println("현재 접속된 DB: " + db.getName());
		System.out.println("컬렉션 목록:");
		db.listCollectionNames().forEach(System.out::println);
	}
}
