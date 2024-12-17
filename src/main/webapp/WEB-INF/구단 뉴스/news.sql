/**********************************/
/* Table Name: 구단 뉴스 */
/**********************************/
DROP TABLE news;

CREATE TABLE news(
    NEWSNO      NUMBER(10)    NOT NULL ,
    NEWSADD     VARCHAR(50)   NOT NULL, 
    NEWSINFO    VARCHAR(50)   NOT NULL, 
    CLUBNO      NUMBER(10)    NOT NULL, 
    ADMINNO     NUMBER(10)    NOT NULL, 
    PRIMARY KEY (NEWSNO),
    FOREIGN KEY (CLUBNO) REFERENCES club (CLUBNO),
    FOREIGN KEY (ADMINNO) REFERENCES admin (ADMINNO)
);

COMMENT ON TABLE news is '구단 뉴스';
COMMENT ON COLUMN news.NEWSNO is '뉴스 번호';
COMMENT ON COLUMN news.NEWSADD is '뉴스 주소';
COMMENT ON COLUMN news.NEWSINFO is '뉴스 내용';


DROP SEQUENCE news_SEQ;

CREATE SEQUENCE news_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지

SELECT newsno, newsadd, newsinfo FROM news ORDER BY newsno;

INSERT INTO news(newsno, newsadd, newsinfo, clubno)
VALUES(news_seq.nextval, 'https://www.premierleague.com/news/4194113', '첼시가 리버풀을 2점차로 쫓고 있다.', '6');

INSERT INTO news(newsno, newsadd, newsinfo)
VALUES(news_seq.nextval, 'https://www.premierleague.com/news/4198011', '분석: 아모링의 영웅 아마드', '13');

