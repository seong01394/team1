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

//SELECT r.commugoodno, r.rdate, r.communo, c.headline as c_headline, r.memberno, m.id, m.name
//FROM commu c, commugood r, member m
//WHERE c.communo = r.communo AND r.memberno = m.memberno
//ORDER BY commugoodno DESC;

@Getter @Setter @ToString
public class CommuCommugoodMemberVO {
  
  /** 커뮤니티 추천 번호 */
  private int commugoodno;
  
  /** 커뮤니티 번호 */
  private int communo;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 등록일 */
  private String rdate; 
  
  /** 커뮤니티 글 제목 */
  private String c_headline = "";
  
  /** 회원 id */
  private String id = "";
  
  /** 회원 이름 */
  private String name = "";
  
}
