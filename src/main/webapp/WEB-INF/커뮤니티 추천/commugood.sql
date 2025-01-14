DROP TABLE commugood;

CREATE TABLE commugood (
	COMMUGOODNO	    NUMBER(10)		NOT NULL,
	COMMUNO	        NUMBER(10)		NOT NULL,
	MEMBERNO	    NUMBER(10)		NOT NULL,
	RDATE	        DATE		    NOT NULL,
    FOREIGN KEY (communo) REFERENCES commu (communo),
    FOREIGN KEY (memberno) REFERENCES member (memberno),
    PRIMARY KEY (commugoodno)
);

DROP SEQUENCE commugood_seq;

CREATE SEQUENCE commugood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '5', '4');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '4', '4');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '3', '6');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '2', '7');

COMMIT;

-- 전체 목록
SELECT commugoodno,rdate, communo, memberno
FROM commugood
ORDER BY commugoodno DESC;

COMMUGOODNO RDATE                  COMMUNO   MEMBERNO
----------- ------------------- ---------- ----------
          4 2025-01-07 10:57:46          2          7
          3 2025-01-07 10:55:05          3          6
          2 2025-01-07 10:55:05          4          4
          1 2025-01-07 10:55:05          5          4

-- PK 조회
SELECT commugoodno,rdate, communo, memberno
FROM commugood
WHERE commugoodno = 1;

COMMUGOODNO RDATE                  COMMUNO   MEMBERNO
----------- ------------------- ---------- ----------
          1 2025-01-07 10:55:05          5          4
          
          
          
-- commugoodno, memberno로 조회          
SELECT commugoodno,rdate, communo, memberno
FROM commugood
WHERE commugoodno=3 AND memberno=6;          

-- 삭제
DELETE FROM commugood
WHERE commugoodno = 4;

commit;


SELECT COUNT(*) as cnt
FROM commugood
WHERE commugoodno=3 AND memberno=6; 

       CNT
---------- 
         1  < --  이미 추천을 함
         
         
SELECT COUNT(*) as cnt
FROM commugood
WHERE commugoodno=1 AND memberno=5;

       CNT
----------
         0  < -- 추천 안됨
         


-- JOIN, 어느 설문을 누가 추천 헀는가?         
SELECT commugoodno,rdate, communo, memberno
FROM commugood
ORDER BY commugoodno DESC;

-- 테이블2개 JOIN
SELECT r.commugoodno, r.rdate, r.communo,c.headline, r.memberno
FROM commu c, commugood r
WHERE c.communo = r.communo
ORDER BY commugoodno DESC;

-- 테이블3개 JOIN,  as 사용시 컬렴명 가능: c.headline as c_headline
SELECT r.commugoodno, r.rdate, r.communo, c.headline as c_headline, r.memberno, m.id, m.name
FROM commu c, commugood r, member m
WHERE c.communo = r.communo AND r.memberno = m.memberno
ORDER BY commugoodno DESC;

         
                
