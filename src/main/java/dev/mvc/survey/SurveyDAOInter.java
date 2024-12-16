package dev.mvc.survey;

import java.util.ArrayList;

public interface SurveyDAOInter {
  
  /**
   * 등록
   * @param surveyVO
   * @return
   */
  public int create(SurveyVO surveyVO);
  /**
   * 조회
   * @param surveyVO
   * @return
   */
  public SurveyVO read(int surveyno);
  /**
   * 수정
   * @param surveyVO
   * @return
   */
  public int update(SurveyVO surveyVO);
  /**
   * 삭제
   * @param surveyVO
   * @return
   */
  public int delete(int surveyno);
  /**
   * title별 등록된 설문조사 목록
   * @param surveyno
   * @return
   */
  public ArrayList<SurveyVO> list_all();
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, int record_per_page,
      int page_per_block);
}

