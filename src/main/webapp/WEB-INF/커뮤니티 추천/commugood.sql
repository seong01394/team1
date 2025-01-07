DROP TABLE commugood;

CREATE TABLE commugood (
	COMMUGOODNO	    NUMBER(10)		NOT NULL,
	COMMUNO	        NUMBER(10)		NOT NULL,
	MEMBERNO	    NUMBER(10)		NOT NULL,
	RDATE	        DATE		    NOT NULL,
    FOREIGN KEY (communo) REFERENCES commu (communo),
    FOREIGN KEY (memberno) REFERENCES member (memberno),
    PRIMARY KEY (commugoodno)
);

DROP SEQUENCE commugood_seq;

CREATE SEQUENCE commugood_seq
START WITH 1         -- 시작 번호
INCREMENT BY 1       -- 증가값
MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
CACHE 2              -- 2번은 메모리에서만 계산
NOCYCLE;             -- 다시 1부터 생성되는 것을 방지


-- 데이터 삽입
INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '5', '4');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '4', '4');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '3', '6');

INSERT INTO commugood(commugoodno,rdate, communo, memberno)
VALUES (commugood_seq.nextval, sysdate, '2', '7');

COMMIT;

-- 전체 목록
SELECT commugoodno,rdate, communo, memberno
FROM commugood
ORDER BY commugoodno DESC;

COMMUGOODNO RDATE                  COMMUNO   MEMBERNO
----------- ------------------- ---------- ----------
          4 2025-01-07 10:57:46          2          7
          3 2025-01-07 10:55:05          3          6
          2 2025-01-07 10:55:05          4          4
          1 2025-01-07 10:55:05          5          4

-- 특정 달의 목록
SELECT commugoodno,rdate, communo, memberno
FROM calendar
WHERE SUBSTR(labeldate, 1, 7) = '2025-01'
ORDER BY labeldate ASC, seqno ASC;

CALENDARNO LABELDATE  LABEL                               SEQNO
---------- ---------- ------------------------------ ----------
         1 2024-12-24 크리스마스 이브                         1
         2 2024-12-25 휴강 안내                               1
         3 2024-12-25 학원 출입 안내                          2

-- 특정 날짜의 목록
SELECT calendarno, labeldate, label, seqno
FROM calendar
WHERE labeldate = '2025-01-01';

CALENDARNO LABELDATE  LABEL                                                   SEQNO
---------- ---------- -------------------------------------------------- ----------
         1 2024-12-24 크리스마스 이브                                             1

-- 조회수 증가
UPDATE calendar
SET cnt = cnt + 1
WHERE calendarno = 1;

-- 조회
SELECT calendarno, labeldate, label, title, content, cnt, regdate, seqno
FROM calendar
WHERE calendarno = 1;

-- 변경
UPDATE calendar
SET labeldate = '', label = '', title = '', content = '', seqno = 1
WHERE calendarno = 1;

-- 삭제
DELETE FROM calendar
WHERE calendarno = 7;

commit;