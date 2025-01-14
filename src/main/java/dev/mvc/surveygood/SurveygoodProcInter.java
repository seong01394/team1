package dev.mvc.surveygood;

import java.util.ArrayList;
import java.util.HashMap;

public interface SurveygoodProcInter  {
  
  // 좋아요 생성
  public int create(SurveygoodVO surveygoodVO);

  // 좋아요 목록
  public ArrayList<SurveygoodVO> list_all();
  
  // 좋아요 삭제
  public int delete(int surveygoodno);
  
  // 특정 설문조사의 특정 회원 추천 갯수 산출
  public int hartCnt(HashMap<String, Object> map);
  
  // 좋아요 조회
  public SurveygoodVO read(int surveygoodno);
  
  // 설문조사 번호 회원 번호 읽어와 조회
  public  SurveygoodVO readBySurveynoMemberno(HashMap<String, Object> map);
  
  // 테이블 3개(survey surveygood member) 조인한 목록
  public ArrayList<SurveySurveygoodMemberVO> join_list_all();
}
