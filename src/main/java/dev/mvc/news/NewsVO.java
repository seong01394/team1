package dev.mvc.news;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE news(
//    NEWSNO            NUMBER(10)    NOT NULL,
//    ADMINNO           NUMBER(10)    NOT NULL,
//    NEWSTITLE         VARCHAR(100)  NOT NULL,
//    NEWSADD           VARCHAR(70)   NOT NULL, 
//    NEWSINFO          VARCHAR(70)   NOT NULL,  
//    RDATE             DATE          NOT NULL,
//    NEWSTHUMB         VARCHAR(300)   NULL,
//    NEWSIMAGE         VARCHAR(300)   NULL,
//    NEWSIMAGESAVED    VARCHAR(300)   NULL,
//    NEWSIMAGESIZE     VARCHAR(300)   DEFAULT 0    NULL, 
//    PRIMARY KEY (NEWSNO),
//    FOREIGN KEY (ADMINNO) REFERENCES admin (ADMINNO)
//);

@Setter @Getter @ToString
public class NewsVO {
  
  /** 뉴스 번호 */
  private int newsno;
  
  /** 관리자 번호*/
  private Integer adminno = 1;
  
  /** 뉴스 제목*/
  private String newstitle;
  
  /** 뉴스 주소*/
  private String newsadd;
  
  /** 뉴스 내용*/
  private String newsinfo;
  
  /** 뉴스 등록일 */
  private String rdate;
  
}