package dev.mvc.survey_topic;

import java.util.ArrayList;






public interface SurveytopicProcInter {
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveytopicVO surveytopicVO);
  /**
   * 조회
   * @param surveyVO
   * @return
   */
  public SurveytopicVO read(int surveytopicno);
  /**
   * 수정
   * @param surveyVO
   * @return
   */
  public int update(SurveytopicVO surveytopicVO);
  /**
   * 삭제
   * @param surveyVO
   * @return
   */
  public int delete(int surveytopicno);
  /**
   * 설문조사별 설문조사 개별 문제 목록
   * @param surveyno
   * @return
   */
  
  ArrayList<SurveytopicVO> listBySurveyno(int surveyno);
}

  



