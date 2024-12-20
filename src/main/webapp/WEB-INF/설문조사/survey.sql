DROP TABLE survey CASCADE CONSTRAINTS;

CREATE TABLE survey (
    surveyno    NUMBER(5)   NOT NULL    PRIMARY KEY,  -- 설문조사 번호
    survey_title    VARCHAR(50)   NOT NULL,  -- 설문조사 타이틀
    start_date      VARCHAR(10)     NOT NULL, -- 시작날
    fin_date        VARCHAR(10)     NULL, -- 완료날
    y_n             CHAR(1)   DEFAULT 'Y' NOT NULL, -- 진행 여부
    cnt             NUMBER(7)   DEFAULT 0 NOT NULL, -- 총 전체 인원 수
    poster          VARCHAR(100)          NULL,  -- 원본 파일명 image
    postersaved      VARCHAR(100)          NULL,  -- 저장된 파일명, image
    posterthumb         VARCHAR(100)          NULL,   -- preview image
    postersize          NUMBER(10)      DEFAULT 0 NULL  -- 파일 사이즈
--    adminno         NUMBER(10)      NOT NULL,  -- 관리자 번호
--  FOREIGN KEY (adminno) REFERENCES admin(adminno)
)
    
COMMENT ON TABLE survey is '설문조사';
COMMENT ON COLUMN survey.surveyno is '설문조사 번호';
COMMENT ON COLUMN survey.survey_title is '설문조사 타이틀';
COMMENT ON COLUMN survey.start_date is '시작날';
COMMENT ON COLUMN survey.fin_date is '완료날';
COMMENT ON COLUMN survey.y_n is '진행 여부'; 
COMMENT ON COLUMN survey.cnt is '총 전체 인원'; 
COMMENT ON COLUMN survey.poster is '원본 포스터파일';
COMMENT ON COLUMN survey.postersaved is '저장한 포스터 파일';
COMMENT ON COLUMN survey.posterthumb is '포스터썸네일';
COMMENT ON COLUMN survey.postersize is '포스터 파일 크기';
--COMMENT ON COLUMN survey.adminno is '관리자 번호';


DROP SEQUENCE SURVEY_SEQ;

CREATE SEQUENCE SURVEY_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO survey(surveyno, survey_title, start_date, fin_date, y_n, cnt, poster, postersaved, posterthumb, postersize)
VALUES(survey_seq.nextval, '상위 3개의 팀 추천 설문조사1-1', '2024-12-01', 'null', 'y', 1, 'image.png', 'image.png', 'image.png', 1309);



- 목록
select surveyno, survey_title, start_date, fin_date, y_n, cnt, poster, postersaved, posterthumb, postersize FROM survey ORDER BY surveyno ASC;

-- 조회
select surveyno, survey_title, start_date, fin_date, y_n, cnt, poster, postersaved, posterthumb, postersize FROM survey WHERE surveyno = 1; 

--수정
UPDATE survey SET qa_contents = '역습 vs 패스 플레이' , seqno = 3 WHERE surveyno=6;

--삭제
DELETE FROM survey WHERE surveyno=6;
select surveyno, survey_title, start_date, fin_date, y_n, cnt, poster, postersaved, posterthumb, postersize FROM survey ORDER BY surveyno ASC;





