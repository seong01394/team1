DROP TABLE surjoin CASCADE CONSTRAINTS;

CREATE TABLE surjoin (
    surjoinno    NUMBER(5)   NOT NULL  PRIMARY KEY,  -- 설문조사 참여 번호
    memberno      NUMBER(10)  NOT NULL,  -- 회원 번호
    surveyno      NUMBER(10)  NOT NULL, -- 설문조사 번호
    partic_date   Date        NOT NULL,  -- 참여 날짜
    selectno      NUMBER(10)  NOT NULL, -- 선택 인원 수
    FOREIGN KEY (memberno) REFERENCES member(memberno),
    FOREIGN KEY (surveyno) REFERENCES survey(surveyno)
)
    
COMMENT ON TABLE surjoin is '설문조사 참여';
COMMENT ON COLUMN surjoin.surjoinno is '설문조사 참여 번호';
COMMENT ON COLUMN surjoin.memberno is '회원 번호';
COMMENT ON COLUMN surjoin.surveyno is '설문조사 번호';
COMMENT ON COLUMN surjoin.partic_date is '참여 날짜';
COMMENT ON COLUMN surjoin.selectno is '선택 인원 수'; 


DROP SEQUENCE SURJOIN_SEQ;

CREATE SEQUENCE SURJOIN_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO surjoin(surjoinno, memberno, surveyno, partic_date, selectno)
VALUES(surjoin_seq.nextval, 1, 1, sysdate, 1);



- 목록
select surjoinno, memberno, surveyno, partic_date, selectno FROM surjoin ORDER BY surjoinno ASC;

-- 조회
select surjoinno, memberno, surveyno, partic_date, selectno FROM surjoin WHERE surjoinno = 1; 

--수정
UPDATE surjoin SET selectno = 2WHERE surjoinno=1;

--삭제
DELETE FROM surjoin WHERE memberno=1;
select surjoinno, memberno, surveyno, partic_date, selectno FROM surjoin ORDER BY surjoinno ASC;





