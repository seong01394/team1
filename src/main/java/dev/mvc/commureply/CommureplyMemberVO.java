package dev.mvc.commureply;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class CommureplyMemberVO {
  /** 아이디(이메일) */
  private String id = "";
  
  /** 댓글 번호 */
  private int commureplyno;

  /** 관련 글 번호 */
  private int communo;
  
  /** 회원 번호 */
  private int memberno;
  
  /** 내용 */
  private String content;
  
  /** 등록일 */
  private String rdate;

  private String name;
}
