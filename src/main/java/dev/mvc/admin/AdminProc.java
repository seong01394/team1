package dev.mvc.admin;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
import jakarta.servlet.http.HttpSession;

@Component("dev.mvc.admin.AdminProc")
public class AdminProc implements AdminProcInter {
  @Autowired
  private AdminDAOInter adminDAO;
  
  @Autowired
  Security security;
  
  public AdminProc() {
    
  }
  
  @Override
  public int checkID(String id) {
    int cnt = this.adminDAO.checkID(id);
    return cnt;
  }

  @Override
  public ArrayList<AdminVO> list() {
    ArrayList<AdminVO> list = this.adminDAO.list();
    return list;
  }
  
  @Override
  public AdminVO read(int adminno) {
    AdminVO adminVO = this.adminDAO.read(adminno);
    return adminVO;
  }

  @Override
  public AdminVO readById(String id) {
    AdminVO adminVO = this.adminDAO.readById(id);
    return adminVO;
  }

  /**
   * 관리자인지 검사
   */
  @Override
  public boolean isAdmin(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin") || grade.equals("member")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }

  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.adminDAO.passwd_check(map);
    return cnt;
  }
  
  @Override
  public int login(HashMap<String, Object> map) {
    String passwd = (String)map.get("passwd");
    passwd = this.security.aesEncode(passwd);
    map.put("passwd", passwd);
    
    int cnt = this.adminDAO.login(map);
    
    return cnt;
  }
  
    
}