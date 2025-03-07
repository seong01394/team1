package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  // 구현 클래스를 교체하기 쉬운 구조 지원

// import javax.servlet.http.HttpSession; // Spring Boot ~ 2.9
import jakarta.servlet.http.HttpSession; //  Spring Boot 3.0~

public interface MemberProcInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String id);
  
  /**
   * 닉네임 중복 검사
   * @param nickname
   * @return
   */
  public int checkNICKNAME(String nickname);
  
  /**
   * 회원 가입
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * 회원 전체 목록
   * @return
   */
  public ArrayList<MemberVO> list();
  
  

  /**
   * memberno로 회원 정보 조회
   * @param memberno
   * @return
   */
  public MemberVO read(int memberno);
  
  /**
   * id로 회원 정보 조회
   * @param id
   * @return
   */
  public MemberVO readById(String id);
  
  /**
   * 로그인된 회원 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMember(HttpSession session);

  /**
   * 로그인된 회원 식물1 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberPlant1(HttpSession session);
  /**
   * 로그인된 회원 식물2 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberPlant2(HttpSession session);
  
  /**
   * 로그인된 회원 식물2 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberPlant3(HttpSession session);
  
  /**
   * 로그인된 회원 관리자 계정인지 검사합니다.
   * @param session
   * @return true: 사용자
   */
  public boolean isMemberAdmin(HttpSession session);
  /**
   * 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
 
  /**
   * 회원 탈퇴 처리
   * @param memberno
   * @return
   */
  public int bye(MemberVO memberVO);
  
  /**
   * 이름, 전화번호 입력받아서 일치하는 회원이 있는지 검사
   * @return 찾은 id
   */
  public String id_check_find(HashMap<String, String> map);
  
  /**
   * 아이디, 전화번호 입력받아서 일치하는 회원이 있는지 검사
   * @return 찾은 id 개수
   */
  public int passwd_check_find(HashMap<String, String> map);
  
  
  /**
   * 문자 인증 성공 시 비밀번호 수정 처리
   * @param map
   * @return 수정한 비밀번호 개수
   */
  public int passwd_update_find(HashMap<String, String> map);
  
  /**
   * 현재 패스워드 검사
   * @param map
   * @return 0: 일치하지 않음, 1: 일치함
   */
  public int passwd_check(HashMap<String, Object> map);
  
  public int id_check(HashMap<String, Object> map);
  
  /**
   * 패스워드 변경
   * @param map
   * @return 변경된 패스워드 갯수
   */
  public int passwd_update(HashMap<String, Object> map);
  
  public int id_update(HashMap<String, Object> map);
  
  /**
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  

}
