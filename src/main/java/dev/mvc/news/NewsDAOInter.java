package dev.mvc.news;

import java.util.ArrayList;
import java.util.Map;

public interface NewsDAOInter {
  
  /**
   * 등록
   * @param commuVO
   * @return
   */
  public int create(NewsVO newsVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<NewsVO> list_all();  
  
  /**
   * 조회
   * @param newsno
   * @return
   */
  public NewsVO read(int newsno);
  
  /**
   * 수정
   * @param newsVO
   * @return
   */
  public int update(NewsVO newsVO);
  
  /**
   * 삭제
   * @param newsno
   * @return
   */
  public int delete(int newsno);
  
  /**
   * 검색 목록 
   * @return
   */
  public ArrayList<NewsVO> list_search(String word);
  
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
  public ArrayList<NewsVO> list_search_paging(Map<String, Object> map);
}