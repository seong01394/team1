package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;  // class
import java.util.List;
// interface, 인터페이스를 사용하는 이유는 다른 형태의 구현 클래스로 변경시 소스 변경이 거의 발생 안됨
// 예) 2022년 세금 계산 방법 구현 class, 2023년 세금 계산 방법 구현 class
// 인터페이스 = 구현 클래스
// Payend pay = new Payend2022();
// Payend pay = new Payend2023();
// Payend pay = new Payend2024();
// pay.calc();
import java.util.Map;

     

public interface MemberDAOInter {
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
   * @return 추가한 레코드 갯수
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
   * 현재 패스워드 검사
   * @param map
   * @return 0: 일치하지 않음, 1: 일치함
   */
  public int passwd_check(HashMap<String, Object> map);
  
  /**
   * 패스워드 변경
   * @param map
   * @return 변경된 패스워드 갯수
   */
  public int passwd_update(HashMap<String, Object> map);
  
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
   * 로그인 처리
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * 이름, 전화번호 입력받아서 일치하는 회원이 있는지 검사
   * @return 찾은 id
   */
  public String id_check_find(HashMap<String, String> map);
  
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_cateno_search_count(HashMap<String, Object> hashMap);
  

  
  /**
   * 현재 아이디 일치 검사 
   * @param map
   * @return
   */
  public int id_check(HashMap<String, Object> map);
  
  /**
   * 아이디 변경
   * @param map
   * @return
   */
  public int id_update(HashMap<String, Object> map);
  

}
 