DROP TABLE surveymember CASCADE CONSTRAINTS;

CREATE TABLE surveymember (
  surveymemberno      NUMBER(10)       NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  rdate        DATE     NOT NULL, -- 패스워드, 영숫자 조합, 암호화
  surveyitemno          NUMBER(5)      NOT NULL, -- 선문조사 항목번호
  memberno      NUMBER(5)      NOT NULL, --회원번호
  FOREIGN KEY (surveyitemno) REFERENCES survey_item(surveyitemno),
  FOREIGN KEY (memberno) REFERENCES member(memberno)
);

COMMENT ON TABLE surveymember is '설문조사참여회원';
COMMENT ON COLUMN surveymember.surveymemberno is '설문조사 참여회원 번호';
COMMENT ON COLUMN surveymember.rdate is '등록일';
COMMENT ON COLUMN surveymember.surveyitemno is '설문조사 항목 번호';
COMMENT ON COLUMN surveymember.memberno is '회원 번호';

DROP SEQUENCE surveymember_seq;

CREATE SEQUENCE surveymember_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 29, 1);
  
  INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 43, 2);
    INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 44, 2);
    INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 45, 2);
  
    INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 46, 2);
    INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 47, 2);
    INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 48, 2);
  
  INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 29, 3);
  INSERT INTO surveymember(surveymemberno, rdate, surveyitemno, memberno)
  VALUES (surveymember_seq.nextval, sysdate, 29, 4);
  
SELECT surveymemberno, rdate, surveyitemno, memberno
FROM surveymember
ORDER BY surveymemberno DESC;

    SELECT r.surveymemberno, r.rdate, r.surveyitemno, c.topic, r.memberno, m.id, m.name, i.item, s.survey_title
    FROM survey_topic c, surveymember r, member m, survey_item i, survey s
    WHERE i.surveyitemno = r.surveyitemno AND r.memberno = m.memberno
    ORDER BY r.surveymemberno DESC;
    
    SELECT r.surveymemberno, r.rdate, r.surveyitemno, c.topic, r.memberno, m.id, m.name, i.item, s.survey_title
    FROM survey_topic c, surveymember r, member m, survey_item i, survey s
    WHERE i.surveyitemno = r.surveyitemno AND r.memberno = m.memberno
    ORDER BY r.surveymemberno DESC
    

commit;