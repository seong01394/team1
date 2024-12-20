/**********************************/
/* Table Name: 축구 커뮤니티 */
/**********************************/
DROP TABLE commu;

CREATE TABLE commu(
    COMMUNO       NUMBER(10)     NOT NULL ,
    CLUBNO        NUMBER(10)     NOT NULL,
    MEMBERNO      NUMBER(10)     NOT NULL,
    HEADLINE      VARCHAR(100)   NOT NULL, 
    CONTENTS      CLOB           NOT NULL, 
    RECOM         NUMBER(7)      DEFAULT 0    NOT NULL,
    REPLY         NUMBER(7)      DEFAULT 0    NOT NULL,
    VIEW          NUMBER(7)      DEFAULT 0    NOT NULL,
    RDATE         DATE           NOT NULL,
    COMMUTHUMB    VARCHAR(300)   NULL,
    IMAGE         VARCHAR(300)   NULL,
    IMAGESAVED    VARCHAR(300)   NULL,
    IMAGESIZE     VARCHAR(300)   DEFAULT 0    NULL, 
    YOUTUBE       VARCHAR(100)   NOT NULL,
    PRIMARY KEY (COMMUNO),
    FOREIGN KEY (CLUBNO) REFERENCES club (CLUBNO),
    FOREIGN KEY (MEMBERNO) REFERENCES member (MEMBERNO)
);


COMMENT ON TABLE commu is '축구 커뮤니티';
COMMENT ON COLUMN commu.COMMUNO is '커뮤 번호';
COMMENT ON COLUMN commu.CLUBNO is '구단 번호';
COMMENT ON COLUMN commu.MEMBERNO is '회원 번호';
COMMENT ON COLUMN commu.HEADLINE is '본문 제목';
COMMENT ON COLUMN commu.CONTENTS is '본문 내용';
COMMENT ON COLUMN commu.RECOM is '추천수';
COMMENT ON COLUMN commu.REPLY is '댓글수';
COMMENT ON COLUMN commu.VIEW is '조회수';
COMMENT ON COLUMN commu.RDATE is '등록일';
COMMENT ON COLUMN commu.COMMUTHUMB is '본문 섬네일';
COMMENT ON COLUMN commu.IMAGE is '메인 이미지';
COMMENT ON COLUMN commu.IMAGESAVED is '저장된 이미지';
COMMENT ON COLUMN commu.IMAGESIZE is '이미지 크기';
COMMENT ON COLUMN commu.YOUTUBE is '유튜브 링크';


DROP SEQUENCE commu_SEQ;

CREATE SEQUENCE commu_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지
  
-- 조회
SELECT communo, clubno, memberno, headline, contents, recom, reply,view, rdate, commuthumb, image, imagesaved, imagesize FROM commu ORDER BY communo;

-- 등록
INSERT INTO match(matchno, clubno, preview, review)
VALUES(match_seq.nextval, '6', '첼시, 2위를 지키기 위해 브랜트포드를 상대한다.', '2-1로 첼시가 승리하며 2위를 지켜내다.');

-- 수정
UPDATE match SET clubno='13', preview='슬럼프에 빠진 맨시티 vs 새 감독 아모링의 맨유', review = '극적인 골들로 맨체스터 더비를 승리한 맨유' WHERE matchno =1; 

-- 삭제
DELETE FROM match WHERE matchno=1;
SELECT matchno, clubno, preview, review FROM match ORDER BY matchno ASC;   
  
  
  
  
  
  
  
  