DROP TABLE survey_item CASCADE CONSTRAINTS;

CREATE TABLE survey_item (
    surveyitemno    NUMBER(5)   NOT NULL    PRIMARY KEY,  -- 설문조사항목 번호
    surveytopicno  NUMBER(10)  NOT NULL, -- 설문조사 개별문제 번호
    item        VARCHAR(200)   NOT NULL,  --  항목 내용
    itemseq        NUMBER(10)  NOT NULL, -- 항목 출력순서
    itemcnt       NUMBER(7)   NOT NULL, -- 선택 인원 수
    FOREIGN KEY (surveytopicno) REFERENCES survey_topic(surveytopicno)
)
    
COMMENT ON TABLE survey_item is '설문조사 항목';
COMMENT ON COLUMN survey_item.surveyitemno is '설문조사항목 번호';
COMMENT ON COLUMN survey_item.surveytopicno is '설문조사 개별문제 번호';
COMMENT ON COLUMN survey_item.item is '항목 내용';
COMMENT ON COLUMN survey_item.itemseq is '항목 출력순서'; 
COMMENT ON COLUMN survey_item.itemcnt is '선택 인원 수'; 
--COMMENT ON COLUMN survey.adminno is '관리자 번호';


DROP SEQUENCE SURVEY_ITEM_SEQ;

CREATE SEQUENCE SURVEY_ITEM_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO survey_item(surveyitemno, surveytopicno, item, itemseq, itemcnt)
VALUES(SURVEY_ITEM_SEQ.nextval, 89, '손흥민, 황희찬 등을 비롯한 한국 선수가 뛰었던 팀 O', 1, 0);

UPDATE survey_item SET itemcnt = itemcnt + 1 WHERE surveyitemno = 1;

- 목록
select surveyitemno, surveytopicno, item, itemseq, itemcnt FROM survey_item ORDER BY surveyitemno ASC;

-- 조회
select surveyitemno, surveytopicno, item, itemseq, itemcnt FROM surveyitem FROM surveyitem WHERE surveyitemno = 1; 

--수정
UPDATE surveyitem SET item = '손흥민, 황희찬 등을 비롯한 한국 선수가 뛰었던 팀 X' WHERE surveyitemno=1;

--삭제
DELETE FROM survey WHERE surveyno=6;
select surveyno, survey_title, start_date, fin_date, y_n, cnt, poster, postersaved, posterthumb, postersize FROM survey ORDER BY surveyno ASC;

SELECT  surveytopicno, surveyno, topic, seqno, file1, filesaved, filethumb, filesize
FROM survey_topic
WHERE surveytopicno= 89

SELECT t.surveyno, t.topic, t.surveytopicno, i.surveyitemno, i.item, i.itemcnt 
FROM survey_item i, survey_topic t
WHERE t.surveytopicno = t.surveytopicno
ORDER BY surveytopicno ASC;


