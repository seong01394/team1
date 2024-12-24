package dev.mvc.survey_topic;

import java.util.ArrayList;
import java.util.Map;





public interface SurveytopicDAOInter {

  // 등록
  public int create(SurveytopicVO surveytopicVO);

  // 조회
  public SurveytopicVO read(int surveytopicno);

  // 수정
  public int update(SurveytopicVO surveytopicVO);

  // 삭제
  public int delete(int surveytopicno);
  
  /**
   * 설문조사별 설문조사 개별 문제 목록
   * @param surveyno
   * @return
   */
  
  ArrayList<SurveytopicVO> listBySurveyno(int surveyno);



  
  
  
}
