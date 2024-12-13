package dev.mvc.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE admin(
//    ADMINNO    NUMBER(10)    NOT NULL,
//    ID         VARCHAR(30)   NOT NULL UNIQUE, -- 아이디, 중복 안됨, 레코드를 구분 
//    PASSWD     VARCHAR(30)   NOT NULL, 
//    ADNAME      VARCHAR(20)   NOT NULL,
//    PRIMARY KEY (ADMINNO)
//);

@Setter @Getter @ToString
public class AdminVO {
  
  /** 관리자 번호 */
  private Integer adminno;
  
  /** 관리자 아이디 */  
  private String id = "";
  
  /** 관리자 비밀번호 */  
  private String passwd = "";
  
  /** 관리자 이름 */  
  private String Adname = "";
  
}