package dev.mvc.news;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE news(
//    NEWSNO      NUMBER(10)    NOT NULL ,
//    NEWSADD     VARCHAR(50)   NOT NULL, 
//    NEWSINFO    VARCHAR(50)   NOT NULL, 
//    ADMINNO     NUMBER(10)    NOT NULL, 
//    PRIMARY KEY (NEWSNO),
//    FOREIGN KEY (ADMINNO) REFERENCES admin (ADMINNO)
//);

@Setter @Getter @ToString
public class NewsVO {
  
  /** 뉴스 번호 */
  private Integer newsno;
  
  /** 관리자 번호*/
  private Integer adminno;
  
  /** 뉴스 주소*/
  private String newsadd;
  
  /** 뉴스 내용*/
  private String newsinfo;
  
}