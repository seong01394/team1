package dev.mvc.commu;

import java.util.ArrayList;
import java.util.HashMap;

import dev.mvc.club.ClubVO;

public interface CommuDAOInter {
  
  /**
   * 등록
   * @param commuVO
   * @return
   */
  public int create(CommuVO commuVO);
  
  /**
   * 목록
   * @return
   */
  public ArrayList<CommuVO> list_all();
  
  
  /**
   * 클럽별 등록된 글 목록
   * @param clubno
   * @return
   */
  public ArrayList<CommuVO> list_by_clubno(int clubno);
  
  
  /**
   * 조회
   * @param communo
   * @return
   */
  public CommuVO read(int communo);
  
  /**
   * youtube 링크
   * @param map
   * @return
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 클럽별 검색 목록
   * @param hashMap
   * @return
   */
  public ArrayList<CommuVO> list_by_clubno_search(HashMap<String, Object> hashMap);
  
  /**
   * 클럽별 검색된 레코드 갯수
   * @param hashMap
   * @return
   */
  public int list_by_clubno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 클럽별 검색 목록 + 페이징
   * @param commuVO
   * @return
   */
  public ArrayList<CommuVO> list_by_clubno_search_paging (HashMap<String, Object> hashMap);
  
  /**
   * 글 수정
   * @param commuVO
   * @return
   */
  public int update_text(CommuVO commuVO);
  
  /**
   * 파일 수정
   * @param commuVO
   * @return
   */
  public int update_file(CommuVO commuVO);
  
  
  /**
   * 삭제
   * @param communo
   * @return
   */
  public int delete(int communo);
  
  
  /**
   * FK clubno 값이 같은 레코드 갯수 산출
   * @param clubno
   * @return
   */
  public int count_by_clubno(int clubno);
  
  
  /**
   * 특정 클럽팀에 속한 모든 레코드 삭제
   * @param clubno
   * @return
   */
  public int delete_by_clubno(int clubno);
  
  
  /**
   * FK memberno 값이 같은 레코드 갯수 산출
   * @param memberno
   * @return
   */
  public int count_by_memberno(int memberno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param memberno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_memberno(int memberno);
  
  /**
   * 글 수 증가
   * @param communo
   * @return
   */
  public int increaseReplycnt(int communo);
  
  /**
   * 글 수 감소
   */
  public int decreaseReplycnt(int communo);
  
  /**
   * 추천 수 증가
   * @param communo
   * @return
   */
  public int increaseRecom(int communo);
  
  /**
   * 추천 수 감소
   */
  public int decreaseRecom(int communo);
  
  /**
   *  추천
   * @param communo
   * @return
   */
  public int good(int communo);
  
  /**
   * join 목록
   * @return
   */
  public ArrayList<CommuMemberVO> list_all_join();
  
}