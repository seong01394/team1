DROP TABLE surres CASCADE CONSTRAINTS;

CREATE TABLE surres (
    resultno    NUMBER(10)   NOT NULL  PRIMARY KEY,  -- 설문조사 참여 번호
    memberno    NUMBER(10)   NOT NULL, -- 회원 번호
    surveyno    NUMBER(10)   NOT NULL, -- 설문조사 번호
    result_contents     VARCHAR(20)     NOT NULL, -- 결과 내용
    FOREIGN KEY (memberno) REFERENCES member(memberno),
    FOREIGN KEY (surveyno) REFERENCES survey(surveyno)
)

DROP SEQUENCE SURRES_SEQ;

CREATE SEQUENCE SURRES_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- 등록
INSERT INTO surres(resultno, memberno, surveyno, result_contents) VALUES(SURRES_SEQ.nextval, 1, 1, '한국인 고름');

-- 조회
select resultno, memberno, surveyno, result_contents FROM surres WHERE resultno = 1; 

--수정
UPDATE surres SET result_contents = '한국인 고르지 않음' WHERE resultno=1;

--삭제
DELETE FROM surres WHERE memberno=1;
select resultno, memberno, surveyno, result_contents FROM surres ORDER BY resultno ASC;