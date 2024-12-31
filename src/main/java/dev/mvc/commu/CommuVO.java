package dev.mvc.commu;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE commu(
//    COMMUNO       NUMBER(10)     NOT NULL ,
//    CLUBNO        NUMBER(10)     NOT NULL,
//    MEMBERNO      NUMBER(10)     NOT NULL,
//    HEADLINE      VARCHAR(100)   NOT NULL, 
//    CONTENTS      CLOB           NOT NULL, 
//    RECOM         NUMBER(7)      DEFAULT 0    NOT NULL,
//    REPLY         NUMBER(7)      DEFAULT 0    NOT NULL,
//    VIEWCNT      NUMBER(7)      DEFAULT 0    NOT NULL,
//    hashtag       VARCHAR(200)   NULL,
//    RDATE         DATE           NOT NULL,
//    COMMUTHUMB    VARCHAR(300)   NULL,
//    IMAGE         VARCHAR(300)   NULL,
//    IMAGESAVED    VARCHAR(300)   NULL,
//    IMAGESIZE     VARCHAR(300)   DEFAULT 0    NULL, 
//    YOUTUBE       VARCHAR(100)    NULL,
//    PRIMARY KEY (COMMUNO),
//    FOREIGN KEY (CLUBNO) REFERENCES club (CLUBNO),
//    FOREIGN KEY (MEMBERNO) REFERENCES member (MEMBERNO)
//);

@Getter @Setter @ToString
public class CommuVO {
  
  /** 커뮤니티 번호 */
  private int communo;
  
  /** 구단 번호 */
  private int clubno;
  
  /** 회원 번호 */
  private int memberno;  
  
  /** 본문 제목 */
  private String headline;
  
  /** 본문 내용 */
  private String contents;  
  
  /** 추천수 */
  private int recom;
  
  /** 댓글수 */
  private int reply = 0;    
  
  /** 조회수 */
  private int viewcnt = 0;
  
  /** 검색 찾기 */
  private String hashtag = "";
  
  /** 등록일 */
  private String rdate = "";  
  
  /** Youtube 링크 */
  private String youtube = "";  
  
  /** 이미지 파일 */
  private MultipartFile file1MF = null;
  
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  
  /** 메인 이미지 */
  private String image = "";
  
  /** 실제 저장된 메인 이미지 */
  private String imagesaved = "";
  
  /** 메인 이미지 preview */
  private String commuthumb = "";
  
  /** 메인 이미지 크기 */
  private long imagesize = 0;
  
  
  
}