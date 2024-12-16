DROP TABLE access_log CASCADE CONSTRAINTS;

CREATE TABLE access_log (
  login_chart   NUMBER(10)  NOT NULL, -- 로그인 내역 번호
  page_chart    NUMBER(10)  NOT NULL, -- 페이지 접속 번호
  ip_address    VARCHAR(20)  NOT NULL, -- ip주소
  login_date    DATE       NOT NULL, -- 로그인 날짜
  memberno      NUMBER(10)  NOT NULL, -- 회원 번호, 레코드를 구분하는 컬럼 
  FOREIGN KEY (memberno) REFERENCES member(memberno) -- 한번 등록된 값은 중복 안됨
);

COMMENT ON TABLE access_log is '접속기록';
COMMENT ON COLUMN access_log.login_chart is '로그인 내역 번호';
COMMENT ON COLUMN access_log.page_chart is '페이지 접속 번호';
COMMENT ON COLUMN access_log.ip_address is 'ip주소';
COMMENT ON COLUMN access_log.login_date is '로그인 날짜';
COMMENT ON COLUMN access_log.memberno is '회원 번호, 레코드를 구분하는 컬럼';

DROP SEQUENCE login_chart_SEQ;

CREATE SEQUENCE login_chart_SEQ
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지

-- CRUD
-- 등록
INSERT INTO access_log(login_chart, page_chart, ip_address, login_date, memberno)
VALUES(login_chart_SEQ.nextval, 1, '192.168.165.12', sysdate, 1);

INSERT INTO access_log(login_chart, page_chart, ip_address, login_date, memberno)
VALUES(login_chart_SEQ.nextval, 2, '192.168.165.10', sysdate, 2);

INSERT INTO access_log(login_chart, page_chart, ip_address, login_date, memberno)
VALUES(login_chart_SEQ.nextval, 3, '192.168.165.15', sysdate, 3);

- 목록
select login_chart, page_chart, ip_address, login_date, memberno FROM access_log ORDER BY memberno ASC;

-- 조회
select login_chart, page_chart, ip_address, login_date, memberno FROM access_log WHERE memberno = 1; 

--수정
UPDATE access_log SET page_chart = 4 WHERE memberno=3;

--삭제
DELETE FROM access_log WHERE memberno=3;
select login_chart, page_chart, ip_address, login_date, memberno FROM access_log ORDER BY memberno ASC;