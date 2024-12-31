package dev.mvc.calendar;

import java.util.ArrayList;
import java.util.Map;

public interface CalendarDAOInter {
  
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
   * @param map
   * @return
   */
  public ArrayList<CalendarVO> list_search_paging(Map<String, Object> map);  
  

}