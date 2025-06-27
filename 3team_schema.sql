CREATE TABLE Member ( -- [회원 테이블]
	  member_id INT PRIMARY KEY AUTO_INCREMENT, -- PK
    nickname VARCHAR(30) NOT NULL UNIQUE, -- 사용자 지정 닉네임
    email VARCHAR(100) NOT NULL UNIQUE, -- 이메일
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 가입 시간
);


CREATE TABLE Post ( -- [게시물(질문) 테이블]
    post_id INT PRIMARY KEY AUTO_INCREMENT, -- PK
    member_id INT NULL, -- 작성자 정보
    title VARCHAR(255) NOT NULL, -- 본문 제목
    content TEXT NOT NULL, -- 본문 내용
    view_cnt INT DEFAULT 0, -- 조회수 
    is_solved TINYINT(1) DEFAULT 0, -- 게시물 상태 (대기중 :0, 해결 :1)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 작성 시간
  
    FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE SET NULL
);

CREATE TABLE Book ( -- [책 테이블]
		book_id INT PRIMARY KEY AUTO_INCREMENT, -- PK
    title VARCHAR(255) NOT NULL, -- 책 제목
    author VARCHAR(60) NOT NULL -- 책 저자
);


CREATE TABLE Tag ( -- [태그(ex:#조용한) 테이블]
	  tag_id INT PRIMARY KEY AUTO_INCREMENT, -- PK
    name VARCHAR(20) NOT NULL UNIQUE -- 태그 이름
);


CREATE TABLE Post_Tag ( -- [Post,tag 다대다 관계 해결 테이블]
	  post_id INT,
    tag_id INT,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES Post(post_id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES Tag(tag_id) ON DELETE CASCADE
);


CREATE TABLE Comment ( -- [댓글(답변) 테이블]
	  comment_id INT PRIMARY KEY AUTO_INCREMENT, -- PK
	  post_id INT, -- 게시물 정보
	  member_id INT NULL, -- 작성자 정보
    content TEXT NOT NULL, -- 내용
    book_id INT NULL, -- 추천 책 정보
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일시
    is_selected TINYINT(1) DEFAULT 0, -- 채택 여부
    FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE SET NULL,
    FOREIGN KEY (post_id) REFERENCES Post(post_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES Book(book_id) ON DELETE SET NULL
);

CREATE TABLE Reaction (
    reaction_id INT PRIMARY KEY AUTO_INCREMENT,  
    -- 각 반응의 고유 ID
    member_id INT NOT NULL,  
    -- 반응을 남긴 사용자 ID (좋아요/싫어요 누른 사람)
    target_type ENUM('POST', 'COMMENT') NOT NULL,  
    -- 어떤 대상에 대한 반응인지 구분 (게시글인지, 답변인지)
    target_id INT NOT NULL,  
    -- 반응 대상의 ID (게시글 ID 또는 답변 ID)
    reaction_type ENUM('LIKE', 'DISLIKE') NOT NULL,  
    -- 반응 유형 (LIKE = 좋아요, DISLIKE = 싫어요)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,  
    -- 반응이 생성된 시간
    UNIQUE (member_id, target_type, target_id),  
    -- 한 사용자가 같은 대상에 대해 한 번만 반응 가능 (좋아요 또는 싫어요 하나만 가능)
    FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE CASCADE 
    -- 사용자 정보 외래키 연결
);