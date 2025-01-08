package dev.mvc.commugood;

import java.util.ArrayList;
import java.util.HashMap;

public interface CommugoodProcInter {

  /**
   *  등록
   * @param commugoodVO
   * @return
   */
  public int create(CommugoodVO commugoodVO);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<CommugoodVO> list_all();
  
  /**
   * 삭제
   * @param Commugoodno
   * @return
   */
  public int delete(int Commugoodno);
  
  /**
   * 특정 컨텐츠의 특정 회원 추천 갯수 산출
   * @param map
   * @return
   */
  public int heartCnt(HashMap<String, Object> map);
  
  /**
   * 조회(PK)
   * @param commugoodno
   * @return
   */
  public CommugoodVO read(int commugoodno);
  
  /**
   * 조회(FK)
   * @param commugoodno
   * @return
   */
  public CommugoodVO readByCommunoMemberno(HashMap<String, Object> map);
  
  /**
   * 전체 목록
   * @return
   */
  public ArrayList<CommuCommugoodMemberVO> list_all_join();
  
}
