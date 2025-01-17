package dev.mvc.LoginLog;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.dto.SearchDTO;

@Component("dev.mvc.LoginLog.LoginLogProc")
public class LoginLogProc implements LoginLogProcInter {

  @Autowired
  private LoginLogDAOInter loginlogDAO;
  
  public LoginLogProc(){
    System.out.println("-> LoginlogProc 생성됨.");
  }
  
  /**
   * 로그인 로그 삽입
   */
  @Override
  public int login_log(LoginLogVO loginLogVO) {
    int cnt = this.loginlogDAO.login_log(loginLogVO);
    return cnt;
  }

  /**
   * 조건에 맞는 로그인 로그 수
   */
  @Override
  public int list_search_count(SearchDTO searchDTO) {
    return this.loginlogDAO.list_search_count(searchDTO);
  }

  /**
   * 로그인 로그 검색 + 페이징
   */
  @Override
  public ArrayList<LoginLogVO> list_search_paging(SearchDTO searchDTO) {
    return this.loginlogDAO.list_search_paging(searchDTO);
  }

  /**
   * 로그인 로그 조회
   */
  @Override
  public LoginLogVO read(int loginlogno) {
    LoginLogVO loginlogVO = this.loginlogDAO.read(loginlogno);
    return loginlogVO;
  }

  /**
   * 로그인 로그 삭제
   */
  @Override
  public int delete(int loginlogno) {
    int cnt = this.loginlogDAO.delete(loginlogno);
    return cnt;
  }



  
}

