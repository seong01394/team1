/**********************************/
/* Table Name: 구단 뉴스 */
/**********************************/
DROP TABLE news;

CREATE TABLE news(
    NEWSNO            NUMBER(10)    NOT NULL,
    ADMINNO           NUMBER(10)    NOT NULL,
    NEWSTITLE         VARCHAR(100)  NOT NULL,
    NEWSADD           VARCHAR(70)   NOT NULL, 
    NEWSINFO          VARCHAR(70)   NOT NULL,  
    RDATE             DATE          NOT NULL,
    PRIMARY KEY (NEWSNO),
    FOREIGN KEY (adminno) REFERENCES admin (adminno)
);

COMMENT ON TABLE news is '구단 뉴스';
COMMENT ON COLUMN news.NEWSNO is '뉴스 번호';
COMMENT ON COLUMN news.ADMINNO is '관리자 번호';
COMMENT ON COLUMN news.NEWSTITLE is '뉴스 제목';
COMMENT ON COLUMN news.NEWSADD is '뉴스 주소';
COMMENT ON COLUMN news.NEWSINFO is '뉴스 내용';
COMMENT ON COLUMN news.rdate is '뉴스 등록일';


DROP SEQUENCE news_SEQ;

CREATE SEQUENCE news_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지

-- 조회
SELECT newsno, adminno, newstitle, newsadd, newsinfo rdate
FROM news
ORDER BY newsno DESC;

-- 등록
INSERT INTO news(newsno, adminno, newstitle, newsadd, newsinfo, rdate) 
VALUES(news_seq.nextval, '1', '치열한 1위싸움', 'https://www.premierleague.com/news/4194113', '첼시가 리버풀을 2점차로 쫓고 있다.', sysdate);


-- 수정
UPDATE news SET newsadd='https://www.premierleague.com/news/4193964', newsinfo = '10명으로 풀럼을 상대하고 있는 리버풀을 구한 조타' WHERE newsno =2; 

-- 삭제
DELETE FROM news WHERE newsno=3;
SELECT newsno, newsadd, newsinfo, clubno FROM news ORDER BY newsno ASC;

commit;


