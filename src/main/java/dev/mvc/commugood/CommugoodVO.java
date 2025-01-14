package dev.mvc.commugood;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE commugood (
//    COMMUGOODNO     NUMBER(10)    NOT NULL,
//    COMMUNO         NUMBER(10)    NOT NULL,
//    MEMBERNO      NUMBER(10)    NOT NULL,
//    RDATE         DATE        NOT NULL,
//      FOREIGN KEY (communo) REFERENCES commu (communo),
//      FOREIGN KEY (memberno) REFERENCES member (memberno),
//      PRIMARY KEY (commugoodno)
//  );

@Getter @Setter @ToString
public class CommugoodVO {
  
  /** 커뮤니티 추천 번호 */
  private int commugoodno;
  
  /** 커뮤니티 번호 */
  private int communo;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 등록일 */
  private String rdate; 

  
}
