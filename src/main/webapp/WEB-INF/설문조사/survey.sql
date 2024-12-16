DROP TABLE survey CASCADE CONSTRAINTS;

CREATE TABLE survey (
    surveyno    NUMBER(5)   NOT NULL,  -- 설문조사 번호
    survey_title    VARCHAR(50)   NOT NULL      PRIMARY KEY,  -- 설문조사 타이틀
    seqno           NUMBER(10)      NOT NULL,  -- 출력 순서
    qa_num          NUMBER(10)      NOT NULL, -- 질문수
    qa_contents     VARCHAR(50)     NOT NULL, -- 질문 내용
    start_date      VARCHAR(10)     NOT NULL, -- 시작날
    fin_date        VARCHAR(10)     NOT NULL, -- 완료날
    write_date      Date            NOT NULL, -- 작성일자
    adminno         NUMBER(10)      NOT NULL,  -- 관리자 번호
    memberno        NUMBER(10)      NOT NULL,  -- 멤버 번호
    FOREIGN KEY (adminno) REFERENCES admin(adminno),
    FOREIGN KEY (memberno) REFERENCES member(memberno)
)
    
COMMENT ON TABLE survey is '설문조사';
COMMENT ON COLUMN survey.surveyno is '설문조사 번호';
COMMENT ON COLUMN survey.survey_title is '설문조사 타이틀';
COMMENT ON COLUMN survey.seqno is '출력 순서';
COMMENT ON COLUMN survey.qa_num is '질문수';
COMMENT ON COLUMN survey.qa_contents is '질문내용'; 
COMMENT ON COLUMN survey.start_date is '시작날';
COMMENT ON COLUMN survey.fin_date is '완료날';
COMMENT ON COLUMN survey.write_date is '작성일자'; 
COMMENT ON COLUMN survey.adminno is '관리자 번호';
COMMENT ON COLUMN survey.adminno is '회원 번호';

DROP SEQUENCE SURVEY_SEQ;

CREATE SEQUENCE SURVEY_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO survey(surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno)
VALUES(survey_seq.nextval, '상위 3개의 팀 추천 설문조사1-1', 1, '2024-12-01', '2024-12-30', 3, '한국인이 있는 팀이 좋습니까?', sysdate, 1, 1);

INSERT INTO survey(surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno)
VALUES(survey_seq.nextval, '상위 3개의 팀 추천 설문조사1-2', 2, '2024-12-01', '2024-12-30', 3, '팀 순위 중 상중하를 선택하세요.', sysdate, 1, 1);

INSERT INTO survey(surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno)
VALUES(survey_seq.nextval, '상위 3개의 팀 추천 설문조사1-3', 3, '2024-12-01', '2024-12-30', 3, '선수 중심, 팀 중심', sysdate, 1, 1);

- 목록
select surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno FROM survey ORDER BY surveyno ASC;

-- 조회
select surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno FROM survey WHERE surveyno = 6; 

--수정
UPDATE survey SET qa_contents = '역습 vs 패스 플레이' , seqno = 3 WHERE surveyno=6;

--삭제
DELETE FROM survey WHERE surveyno=6;
select surveyno, survey_title, seqno, start_date, fin_date, qa_num, qa_contents, write_date, adminno, memberno FROM survey ORDER BY surveyno ASC;





