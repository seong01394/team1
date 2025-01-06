package dev.mvc.survey_item;


import java.util.ArrayList;
import java.util.List;

public interface SurveyitemProcInter {
  // 등록
  public int create(SurveyitemVO surveyitemVO);

  // 조회
  public SurveyitemVO read(int surveyitemno);

  // 수정
  public int update(SurveyitemVO surveyitemVO);

  // 삭제
  public int delete(int surveyitemno);
  
  // 선택인원 수 증가
  public int increaseitemCnt(int surveyitemno);

  
  // 하나의 설문조사에 대한 개별문제 리스트 출력 
  public ArrayList<SurveyitemVO>listBySurveytopicno(int surveytopicno);
}
