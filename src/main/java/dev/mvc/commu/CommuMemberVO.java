package dev.mvc.commu;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//SELECT c.communo, c.clubno, c.memberno, c.headline, c.contents, c.recom, c.reply, c.viewcnt, c.hashtag, c.rdate, c.commuthumb, 
//c.image, c.imagesaved, c.imagesize, m.nickname
//FROM commu c, member m
//WHERE c.memberno = m.memberno
//ORDER BY communo DESC;  

@Getter @Setter @ToString
public class CommuMemberVO {
  
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
  
  /** 회원 닉네임 */
  private String nickname = "";
  
}