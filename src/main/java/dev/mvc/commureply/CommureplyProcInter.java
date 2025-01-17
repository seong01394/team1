package dev.mvc.commureply;

import java.util.List;

public interface CommureplyProcInter {
  
  /**
   * 등록
   * @param commureplyVO
   * @return
   */
  public int create(CommureplyVO commureplyVO); 
  
  /**
   * 글 조회시 댓글 목록 출력
   * @param communo
   * @return
   */
  public List<CommureplyMemberVO> list_by_communo_join(int communo);
  
  
}