package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;



public interface AdminProcInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String id);
  
  /**
   * 관리자 전체 목록
   * @return
   */
  public ArrayList<AdminVO> list();

  /**
   * memberno로 관리자 정보 조회
   * @param adminno
   * @return
   */
  public AdminVO read(int adminno);
  
  /**
   * id로 관리자 정보 조회
   * @param id
   * @return
   */
  public AdminVO readById(String id);
  
  /**
   * 로그인된 관리자 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isAdmin(HttpSession session);

  /**
   * 현재 패스워드 검사
   * @param map
   * @return 0: 일치하지 않음, 1: 일치함
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
}