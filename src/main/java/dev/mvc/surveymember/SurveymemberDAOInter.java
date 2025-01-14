package dev.mvc.surveymember;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveymemberDAOInter {

  /**
   * 테이블 3개 조인
   * @return
   */
  public ArrayList<SurveytopicitemmemberVO> list_all_join();
  
  
}

