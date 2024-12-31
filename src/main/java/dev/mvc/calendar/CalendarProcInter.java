package dev.mvc.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface CalendarProcInter {
  
  /**
   * 등록
   * @param calendarVO
   * @return
   */
  public int create(CalendarVO calendarVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<CalendarVO> list_all();
  
  /**
   * 특정 월의 일정 목록
   * @return
   */
  public ArrayList<CalendarVO> findByMonth();
  
  /**
   * 조회
   * @param calendarno
   * @return
   */
  public CalendarVO read(Integer calendarno);
  
  
  /**
   * 수정
   * @param calendarVO
   * @return
   */
  public int update(CalendarVO calendarVO);
  
  /**
   *  삭제
   * @param calenderno
   * @return
   */
  public int delete(int calenderno);
  
  /**
   * 검색 목록 
   * @return
   */
  public ArrayList<CalendarVO> list_search(String word);
  
  /**
   * 검색 갯수
   * @param word
   * @return
   */
  public Integer list_search_count(String word);
  
 
  /**
   * 검색 + 페이징 목록
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<CalendarVO> list_search_paging(String word, int now_page, int record_per_page);
  
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */
  String pagingBox(int now_page, String word, String list_file_name, int search_count, int record_per_page,
      int page_per_block);
  
}