/**********************************/
/* Table Name: 관리자 */
/**********************************/
DROP TABLE admin;

CREATE TABLE admin(
    ADMINNO    NUMBER(10)    NOT NULL,
    ID         VARCHAR(30)   NOT NULL UNIQUE, -- 아이디, 중복 안됨, 레코드를 구분 
    PASSWD     VARCHAR(30)   NOT NULL, 
    ADNAME      VARCHAR(20)   NOT NULL,
    PRIMARY KEY (ADMINNO)
);

COMMENT ON TABLE admin is '관리자';
COMMENT ON COLUMN admin.ADMINNO is '관리자 번호';
COMMENT ON COLUMN admin.ID is '관리자 아이디';
COMMENT ON COLUMN admin.PASSWD is '관리자 패스워드';
COMMENT ON COLUMN admin.ADNAME is '관리자 이름';

DROP SEQUENCE admin_SEQ;

CREATE SEQUENCE admin_SEQ
  START WITH 1              -- 시작 번호
  INCREMENT BY 1          -- 증가값
  MAXVALUE 9999999999 -- 최대값: 9999999999
  CACHE 2                     -- 2번은 메모리에서만 계산
  NOCYCLE;                   -- 다시 1부터 생성되는 것을 방지

SELECT adminno, id, passwd, adname FROM admin ORDER BY adminno;

INSERT INTO admin(adminno, id, passwd, adname)
VALUES(admin_seq.nextval, 'emp1', '1234', '직원1');

INSERT INTO admin(adminno, id, passwd, adname)
VALUES(admin_seq.nextval, 'emp2', '1234', '직원2');

SELECT * FROM admin WHERE adminno = 1;

