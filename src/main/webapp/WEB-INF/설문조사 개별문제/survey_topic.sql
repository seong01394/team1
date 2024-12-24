DROP TABLE survey_topic CASCADE CONSTRAINTS;

CREATE TABLE survey_topic (
    surveytopicno    NUMBER(10)   NOT NULL    PRIMARY KEY,  -- 설문조사(개별문제) 번호
    surveyno        NUMBER(10)    NOT NULL,
    topic    VARCHAR(200)   NOT NULL,  -- 제목
    seqno      NUMBER(5)     NOT NULL, -- 출력순서
    file1       VARCHAR(100)     NOT NULL,    -- 원본 파일명
    filesaved      VARCHAR(100)    NOT NULL,  -- 저장된 파일명, image
    filethumb         VARCHAR(100)  NOT NULL,   -- preview image
    filesize          NUMBER(10)  NOT NULL,  -- 파일 사이즈
    FOREIGN KEY (surveyno)    REFERENCES survey(surveyno)  -- 설문조사 번호
    
)
    
COMMENT ON TABLE survey_topic is '설문조사 개별문제';
COMMENT ON COLUMN survey_topic.surveytopicno is '설문조사(개별문제) 번호';
COMMENT ON COLUMN survey_topic.survey is '설문조사 번호';
COMMENT ON COLUMN survey_topic.topic is '제목';
COMMENT ON COLUMN survey_topic.seqno is '시작날';
COMMENT ON COLUMN survey_topic.file1 is '원본 파일';
COMMENT ON COLUMN survey_topic.filesaved is '저장된 파일명';
COMMENT ON COLUMN survey_topic.filethumb is '썸네일'; 
COMMENT ON COLUMN survey_topic.filesize is '파일 사이즈'; 
--COMMENT ON COLUMN survey.adminno is '관리자 번호';


DROP SEQUENCE SURVEY_TOPIC_SEQ;

CREATE SEQUENCE SURVEY_TOPIC_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO survey_topic(surveytopicno, topic, seqno, filesaved, filethumb, filesize)
VALUES(SURVEY_TOPIC_SEQ.nextval, '한국인 선수 유무', 1, 'main.jpg', 'main.jpg', 1309);



- 목록
select surveytopicno, topic, seqno, filesaved, filethumb, filesize FROM survey_topic ORDER BY surveytopicno ASC;

-- 조회
select surveytopicno, topic, seqno, filesaved, filethumb, filesize FROM survey_topic WHERE surveyno = 1; 

--수정
UPDATE survey_topic SET topic = '역습 vs 패스 플레이' , seqno = 2 WHERE surveytopicno=1;

--삭제
DELETE FROM survey_topic WHERE surveytopicno=1;
select surveytopicno, topic, seqno, filesaved, filethumb, filesize FROM survey_topic ORDER BY surveytopicno ASC;





