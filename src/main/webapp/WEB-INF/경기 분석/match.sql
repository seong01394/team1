/**********************************/
/* Table Name: 경기 분석 */
/**********************************/
DROP TABLE match;

CREATE TABLE match(
    MATCHNO      NUMBER(10)     NOT NULL ,
    PREVIEW      VARCHAR(255)   NOT NULL, 
    REVIEW       VARCHAR(255)   NOT NULL, 
    CLUBNO      NUMBER(10)    NOT NULL,  
    PRIMARY KEY (MATCHNO),
    FOREIGN KEY (CLUBNO) REFERENCES club (CLUBNO)
);

COMMENT ON TABLE match is '경기 분석';
COMMENT ON COLUMN match.MATCHNO is '경기 번호';
COMMENT ON COLUMN match.CLUBNO is '구단 번호';
COMMENT ON COLUMN match.PREVIEW is '경기 프리뷰';
COMMENT ON COLUMN match.REVIEW is '경기 리뷰';

DROP SEQUENCE match_SEQ;

CREATE SEQUENCE match_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지
  
  
-- 조회
SELECT matchno, clubno, preview, review FROM match ORDER BY matchno;

-- 등록
INSERT INTO match(matchno, clubno, preview, review)
VALUES(match_seq.nextval, '6', '첼시, 2위를 지키기 위해 브랜트포드를 상대한다.', '2-1로 첼시가 승리하며 2위를 지켜내다.');

-- 수정
UPDATE match SET clubno='13', preview='슬럼프에 빠진 맨시티 vs 새 감독 아모링의 맨유', review = '극적인 골들로 맨체스터 더비를 승리한 맨유' WHERE matchno =1; 

-- 삭제
DELETE FROM match WHERE matchno=1;
SELECT matchno, clubno, preview, review FROM match ORDER BY matchno ASC;  