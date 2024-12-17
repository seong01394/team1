/**********************************/
/* Table Name: 구단 */
/**********************************/
DROP TABLE club CASCADE CONSTRAINTS;
CREATE TABLE club (
  CLUBNO        NUMBER(10)        NOT NULL  PRIMARY KEY,
  ADMINNO       NUMBER(10)        NOT NULL,  
  CLUBNAME      VARCHAR(30)       NOT NULL,
  PLAYER        VARCHAR(30)       NOT NULL,
  HEADCOACH     VARCHAR(30)       NOT NULL,
  LEGEND        VARCHAR(30)       NOT NULL,
  HISTORY       CLOB              NOT NULL,
  INFO          VARCHAR(255)      NOT NULL,
  RANK          NUMBER(5)         DEFAULT 1   NOT NULL,
  visible      CHAR(1)           DEFAULT 'Y' NOT NULL,
  FOREIGN KEY (ADMINNO) REFERENCES admin (ADMINNO)
);

COMMENT ON TABLE club is '구단';
COMMENT ON COLUMN club.CLUBNO is '구단 번호';
COMMENT ON COLUMN club.ADMINNO is '관리자 번호';
COMMENT ON COLUMN club.CLUBNAME is '구단명';
COMMENT ON COLUMN club.PLAYER is '구단 주요 선수';
COMMENT ON COLUMN club.HEADCOACH is '구단 감독';
COMMENT ON COLUMN club.LEGEND is '구단 역대 선수';
COMMENT ON COLUMN club.HISTORY is '구단 역사';
COMMENT ON COLUMN club.INFO is '구단 정보';
COMMENT ON COLUMN club.RANK is '출력 순서';
COMMENT ON COLUMN club.visible is '출력 모드';

DROP SEQUENCE club_SEQ;
CREATE SEQUENCE club_SEQ
  START WITH 1             
  INCREMENT BY 1            
  MAXVALUE 9999999999       
  CACHE 2                   
  NOCYCLE;                  
  
-- 등록  
INSERT INTO club(clubno, adminno, clubname, player, headcoach, legend, history, info, rank, visible)
VALUES (club_seq.nextval, 1,'리버풀', '모하메드 살라', '아르네 슬롯', '스티븐 제라드', 
            '리버풀 FC는 영국 잉글랜드 머지사이드 주의 리버풀을 연고로 하는 프로 축구 구단이다. 잉글랜드 프리미어 리그에 소속되어 있으며, 홈구장은 안필드다.', 
            '살라는 현재 리버풀에서 7년째 에이스로서 활약하고 있다',1,'Y');
            
INSERT INTO club(clubno, adminno, clubname, player, headcoach, legend, history, info, rank, visible)
VALUES (club_seq.nextval, 1, '맨체스터 시티', '로드리', '펩 과르디올라', '세르히오 아구에로', 
        '맨시티는 영국 축구 역사상 유일한 5관왕과 잉글랜드 축구 역사상 유일한 도메스틱 트레블(Domestic Treble) 및 리그 4연패를 달성한 클럽이다.', 
        '로드리는 맨시티 역사상 첫 발롱도르를 받았다.', 5,'Y');         
  
-- 전체 목록
SELECT clubno, adminno, clubname, player, headcoach, legend, history, info, rank, visible
FROM club
ORDER BY clubno ASC;  

    CLUBNO    ADMINNO CLUBNAME                       PLAYER                         HEADCOACH                      LEGEND                         HISTORY                                                                          INFO                                                                                   RANK
---------- ---------- ------------------------------ ------------------------------ ------------------------------ ------------------------------ -------------------------------------------------------------------------------- -------------------------------------------------------------------------------- ----------
         1          1 리버풀                         모하메드 살라                  아르네 슬롯                    스티븐 제라드                  리버풀 FC는 영국 잉글랜드 머지사이드 주의 리버풀을 연고로 하는 프로 축구 구단이다. 잉글랜드 프리미어 리그에 소속되어 있으며, 홈구장은 안필 살라는 현재 리버풀에서 7년째 에이스로서 활약하고 있다        1
         2          1 맨체스터 시티                  로드리                         펩 과르디올라                  세르히오 아구에로              맨시티는 영국 축구 역사상 유일한 5관왕과 잉글랜드 축구 역사상 유일한 도메스틱 트레블(Domestic Treble) 및 리그 4연패를 달성한  로드리는 맨시티 역사상 첫 발롱도르를 받았다.                              5

-- 조회
SELECT clubno, adminno, clubname, player, headcoach, legend, history, info, rank, visible
FROM club
WHERE clubno=1; 

    CLUBNO    ADMINNO CLUBNAME                       PLAYER                         HEADCOACH                      LEGEND                         HISTORY                                                                          INFO                                                                                   RANK
---------- ---------- ------------------------------ ------------------------------ ------------------------------ ------------------------------ -------------------------------------------------------------------------------- -------------------------------------------------------------------------------- ----------
         1          1 리버풀                         모하메드 살라                  아르네 슬롯                    스티븐 제라드                  리버풀 FC는 영국 잉글랜드 머지사이드 주의 리버풀을 연고로 하는 프로 축구 구단이다. 잉글랜드 프리미어 리그에 소속되어 있으며, 홈구장은 안필 살라는 현재 리버풀에서 7년째 에이스로서 활약하고 있다          1

