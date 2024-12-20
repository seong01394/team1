package dev.mvc.club;

import java.util.ArrayList;
import java.util.Map;




public interface ClubDAOInter {
  
  /**
   * 등록
   * @param clubVO
   * @return
   */
  public int create(ClubVO clubVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<ClubVO> list_all();
  
  /**
   * 조회
   * @param clubno
   * @return
   */
  public ClubVO read(Integer clubno);
  
  /**
   * 수정
   * @param clubVO
   * @return
   */
  public int update(ClubVO clubVO);
  
  /**
   * 삭제
   * @param clubno
   * @return
   */
  public int delete(int clubno);
  
  /**
   * 구단 순위 높임, 20 등 -> 1 등
   * @param clubno
   * @return
   */
  public int update_rank_up(int clubno);
  
  /**
   * 구단 순위 낮춤, 1 등 -> 20 등
   * @param clubno
   * @return
   */
  public int update_rank_down(int clubno);
  
  /**
   * 구단 목록
   * @return
   */
  public ArrayList<String> clubnameset();
  
  /**
   * 숨긴 '구단 명'을 제외하고 접속자에게 공개할 '구단명' 출력
   * @return
   */
  public ArrayList<ClubVO> list_all_clubgrp_y();  

  /**
   * 숨긴 '구단명들'을 제외하고 접속자에게 공개할 '구단명' 출력"
   * @return
   */
  public ArrayList<ClubVO> list_all_club_y(String clubname);  
  
  /**
   * 검색 목록 
   * @return
   */
  public ArrayList<ClubVO> list_search(String word);
  
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
  public ArrayList<ClubVO> list_search_paging(Map<String, Object> map);
  
  
}