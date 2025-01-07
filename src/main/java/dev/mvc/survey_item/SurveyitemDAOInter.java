package dev.mvc.survey_item;


import java.util.ArrayList;
import java.util.Map;

import dev.mvc.survey_topic.SurveytopicVO;



public interface SurveyitemDAOInter {
  // 등록
  public int create(SurveyitemVO surveyitemVO);

  // 조회
  public SurveyitemVO read(int surveyitemno);

  // 수정
  public int update(SurveyitemVO surveyitemVO);

  // 삭제
  public int delete(int surveyitemno);
  


  // 하나의 설문조사에 대한 개별문제 리스트 출력 
  public ArrayList<SurveyitemVO>listBySurveytopicno(int surveytopicno);
  
  // 선택 인원 수 증가
  public int increaseItemCount(String item);
  
  // 검색 개수
  public Integer list_search_count(String word);

  // 검색 목록
  public ArrayList<SurveyitemVO> list_search(String word);

  // 페이징 목록
  public ArrayList<SurveyitemVO> list_paging(Map<String, Object> map);

  // 검색 + 페이징
  public ArrayList<SurveyitemVO> list_search_paging(Map<String, Object> map);

  
}
