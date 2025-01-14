DROP TABLE member CASCADE CONSTRAINTS;

CREATE TABLE member (
  memberno      NUMBER(10)       NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  id            VARCHAR(30)      NOT NULL UNIQUE, -- 아이디(이메일), 중복 안됨, 레코드를 구분 
  passwd        VARCHAR(200)     NOT NULL, -- 패스워드, 영숫자 조합, 암호화
  name          VARCHAR(30)      NOT NULL, -- 성명, 한글 10자 저장 가능
  nickname      VARCHAR(30)      NOT NULL, -- 별명, 한글+영어 10자 저장 가능
  user_level    VARCHAR(10)      NOT NULL, -- 레벨
  tel           VARCHAR(15)      NOT NULL, -- 전화번호, 국제 번호 고려
  zipcode       CHAR(5)          NULL, -- 우편번호, 12345 (고정된 길이)
  address1      VARCHAR(200)     NULL, -- 주소 1, 길이 확장
  address2      VARCHAR(100)     NULL, -- 주소 2, 길이 확장
  signup_date   DATE             NOT NULL, -- 가입일 
  signup_path   VARCHAR(100)     NOT NULL, -- 가입 경로
  grade         VARCHAR(2)       NOT NULL,   
  PRIMARY KEY (memberno) -- 기본 키, 중복 불가
);
 
COMMENT ON TABLE MEMBER is '회원';
COMMENT ON COLUMN MEMBER.MEMBERNO is '회원 번호';
COMMENT ON COLUMN MEMBER.ID is '아이디';
COMMENT ON COLUMN MEMBER.PASSWD is '패스워드';
COMMENT ON COLUMN MEMBER.NAME is '성명';
COMMENT ON COLUMN MEMBER.NICKNAME is '성명';
COMMENT ON COLUMN MEMBER.USER_LEVEL is '레벨';
COMMENT ON COLUMN MEMBER.TEL is '전화번호';
COMMENT ON COLUMN MEMBER.ZIPCODE is '우편번호';
COMMENT ON COLUMN MEMBER.ADDRESS1 is '주소1';
COMMENT ON COLUMN MEMBER.ADDRESS2 is '주소2';
COMMENT ON COLUMN MEMBER.SIGNUP_DATE is '가입일';
COMMENT ON COLUMN MEMBER.SIGNUP_PATH is '가입경로';
COMMENT ON COLUMN MEMBER.GRADE is '등급';

DROP SEQUENCE member_seq;

CREATE SEQUENCE member_seq
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999 --> NUMBER(7) 대응
  CACHE 2                       -- 2번은 메모리에서만 계산
  NOCYCLE;                     -- 다시 1부터 생성되는 것을 방지

1. 등록
INSERT INTO member(memberno, id, passwd, name, nickname, user_level, tel, zipcode,
                   address1, address2, signup_date, signup_path, grade)
VALUES (member_seq.nextval, 'admin', 'fS/kjO+fuEKk06Zl7VYMhg==', '회원1', '피린이', 'plant', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, '축구 중계보는데 팝업창을 보고 가입하러 왔습니다', 1);

INSERT INTO member(memberno, id, passwd, name, nickname, user_level, tel, zipcode,
                   address1, address2, signup_date, signup_path, grade)
VALUES (member_seq.nextval, 'user1', 'fS/kjO+fuEKk06Zl7VYMhg==', '회원2', '피린이', 'plant', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, '축구 중계보는데 팝업창을 보고 가입하러 왔습니다', 10); 

INSERT INTO member(memberno, id, passwd, name, nickname, user_level, tel, zipcode,
                   address1, address2, signup_date, signup_path, grade)
VALUES (member_seq.nextval, 'user2', 'fS/kjO+fuEKk06Zl7VYMhg==', '회원3', '피린이', 'plant', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, '축구 중계보는데 팝업창을 보고 가입하러 왔습니다', 10); 

INSERT INTO member(memberno, id, passwd, name, nickname, user_level, tel, zipcode,
                   address1, address2, signup_date, signup_path, grade)
VALUES (member_seq.nextval, 'user3', 'fS/kjO+fuEKk06Zl7VYMhg==', '회원4', '피린이', 'plant', '000-0000-0000', '12345',
             '서울시 종로구', '관철동', sysdate, '축구 중계보는데 팝업창을 보고 가입하러 왔습니다', 10);      
     
3. 조회
 
1) 사원 정보 조회
SELECT memberno, id, passwd, name, user_level, tel, zipcode,address1, address2, signup_date, signup_path
FROM member
WHERE memberno = 1;

SELECT memberno, id, passwd, name, user_level, tel, zipcode,address1, address2, signup_date, signup_path
FROM member
WHERE id = 'user1@gmail.com';
 
    
4. 수정, PK: 변경 못함, UNIQUE: 변경을 권장하지 않음(id)
UPDATE member 
SET name='회원2', tel='111-1111-1111', zipcode='00000',
    address1='강원도', address2='홍천군'
WHERE memberno=1;

COMMIT;

 
5. 삭제
1) 모두 삭제
DELETE FROM member;
 
2) 특정 회원 삭제
DELETE FROM member
WHERE memberno=12;

COMMIT;



