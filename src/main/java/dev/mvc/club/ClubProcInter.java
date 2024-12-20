package dev.mvc.club;

import java.util.ArrayList;

public interface ClubProcInter {
  
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
   * 화면 상단 메뉴
   * @return
   */
  public ArrayList<ClubVOMenu> menu();
  
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
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호: 1 ★
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return
   */
  public ArrayList<ClubVO> list_search_paging(String word, int now_page, int record_per_page);

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
  
  /**
   * 파일 정보 수정
   */
  public int update_emblem(ClubVO clubVO);
}