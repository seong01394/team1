package dev.mvc.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest; 
import jakarta.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVOMenu;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/admin")
@Controller
public class AdminCont {
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;  
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  public AdminCont() {
    System.out.println("-> AdminCont created");
  }
  
  @GetMapping(value="/checkID")
  @ResponseBody
  public String checkID(@RequestParam(name="id", defaultValue="") String id) {
    
    int cnt = this.adminProc.checkID(id);
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  @GetMapping(value="/list")
  public String list(HttpSession session, Model model) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    if(this.adminProc.isAdmin(session)) {
      ArrayList<AdminVO> list = this.adminProc.list();
      
      model.addAttribute("list", list);
      
      return "/admin/list";
    } else {
      return "redirect:/admin/login_cookie_need";
   }
 }   
  
  @GetMapping(value="/read")
  public String read(HttpSession session,
                          Model model,
                          @RequestParam(name="adminno", defaultValue="") int adminno) {
    
    Integer sessionadminno = (Integer) session.getAttribute("adminno");
    
    if (sessionadminno == null) {
        return "redirect:/admin/login_cookie_need"; 
    }

    // 일반 사용자: 자신의 정보만 열람 가능
    if (sessionadminno == adminno) {
        System.out.println("-> read adminno: " + adminno);
        
        AdminVO adminVO = this.adminProc.read(adminno);
        model.addAttribute("adminVO", adminVO);
        
        return "admin/read";
    }
    
    System.out.println("-> read adminno: " + adminno);

    AdminVO adminVO = this.adminProc.read(adminno);
    model.addAttribute("adminVO", adminVO);

    return "admin/read";
  }
  
  /**
   * 로그아웃
   * @param model
   * @param adminno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate();  
    return "redirect:/";
  }

  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 시작
  // ----------------------------------------------------------------------------------
  /**
   * 로그인
   * @param model
   * @param adminno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login")
  public String login_form(Model model, HttpServletRequest request) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = "admin"; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = "1234"; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){                     // 아이디
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){        // 아이디 저장 여부
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){       // 패스워드
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){ // 패스워드 저장 여부
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
 
    model.addAttribute("ck_id", ck_id);
  
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

    return "/member/login_cookie";  // /templates/member/login_cookie.html
  }  
  

  /**
   * Cookie 기반 로그인 처리
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id 아이디
   * @param passwd 패스워드
   * @param id_save 아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
 @PostMapping(value="/login")
  public String login_proc(HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model, 
                                     @RequestParam(value="id", defaultValue = "") String id, 
                                     @RequestParam(value="passwd", defaultValue = "") String passwd,
                                     @RequestParam(value="id_save", defaultValue = "") String id_save,
                                     @RequestParam(value="passwd_save", defaultValue = "") String passwd_save
                                     ) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("passwd", passwd);
    
    int cnt = this.adminProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);
    
    model.addAttribute("cnt", cnt);
    
    if (cnt == 1) {
      // id를 이용하여 회원 정보 조회
      AdminVO adminVO = this.adminProc.readById(id);
      session.setAttribute("adminno", adminVO.getAdminno());
      session.setAttribute("id", adminVO.getId());
      session.setAttribute("adname", adminVO.getAdname());
      
      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie ck_id = new Cookie("ck_id", id);
        ck_id.setPath("/");  // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(ck_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie ck_id = new Cookie("ck_id", "");
        ck_id.setPath("/");
        ck_id.setMaxAge(0); // 0초
        response.addCookie(ck_id); // id 저장
      }
      
      // id를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
      ck_id_save.setPath("/");
      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_id_save);
      // -------------------------------------------------------------------
  
      // -------------------------------------------------------------------
      // Password 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie ck_passwd = new Cookie("ck_passwd", passwd);
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(ck_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie ck_passwd = new Cookie("ck_passwd", "");
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(0);
        response.addCookie(ck_passwd);
      }
      // passwd를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
      ck_passwd_save.setPath("/");
      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------     
      
      return "redirect:/";
    } else {
      model.addAttribute("code", "login_fail");
      return "/member/msg";
    }
 }
  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------  
 
 /**
  * 현재 패스워드 확인
  * @param session
  * @param current_passwd
  * @return 1: 일치, 0: 불일치
  */
 @PostMapping(value="/passwd_check")
 @ResponseBody
 public String passwd_check(HttpSession session, @RequestBody String json_src) {
   System.out.println("-> json_src: " + json_src); 
   
   JSONObject src = new JSONObject(json_src); 
   
   String current_passwd = (String)src.get("current_passwd");
   System.out.println("-> current_passwd: " + current_passwd);
   
   try {
     Thread.sleep(3000);
   } catch(Exception e) {
     
   }
   
   int adminno = (int)session.getAttribute("adminno");
   HashMap<String, Object> map = new HashMap<String, Object>();
   map.put("adminno", adminno);
   map.put("passwd", current_passwd);
   
   int cnt = this.adminProc.passwd_check(map); 
   
   JSONObject json = new JSONObject();
   json.put("cnt", cnt);  
   System.out.println(json.toString());
   
   return json.toString();   
 }
  
 /**
  * 로그인 요구에 따른 로그인 폼 출력 
  * @param model
  * @param adminno 회원 번호
  * @return 회원 정보
  */
 @GetMapping(value="/login_cookie_need")
 public String login_cookie_need(Model model, HttpServletRequest request) {
   // Cookie 관련 코드---------------------------------------------------------
   Cookie[] cookies = request.getCookies();
   Cookie cookie = null;
   
   String ck_id = "admin"; // id 저장
   String ck_id_save = ""; // id 저장 여부를 체크
   String ck_passwd = "1234"; // passwd 저장
   String ck_passwd_save = ""; // passwd 저장 여부를 체크
 
   if (cookies != null) { // 쿠키가 존재한다면
     for (int i=0; i < cookies.length; i++){
       cookie = cookies[i]; // 쿠키 객체 추출
     
       if (cookie.getName().equals("ck_id")){
         ck_id = cookie.getValue();  // email
       }else if(cookie.getName().equals("ck_id_save")){
         ck_id_save = cookie.getValue();  // Y, N
       }else if (cookie.getName().equals("ck_passwd")){
         ck_passwd = cookie.getValue();         // 1234
       }else if(cookie.getName().equals("ck_passwd_save")){
         ck_passwd_save = cookie.getValue();  // Y, N
       }
     }
   }
   // ----------------------------------------------------------------------------
   
   //    <input type='text' class="form-control" name='id' id='id' 
   //            th:value='${ck_id }' required="required" 
   //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
   model.addAttribute("ck_id", ck_id);
 
   //    <input type='checkbox' name='id_save' value='Y' 
   //            th:checked="${ck_id_save == 'Y'}"> 저장
   model.addAttribute("ck_id_save", ck_id_save);
 
   model.addAttribute("ck_passwd", ck_passwd);
   model.addAttribute("ck_passwd_save", ck_passwd_save);

//   model.addAttribute("ck_id_save", "Y");
//   model.addAttribute("ck_passwd_save", "Y");
   
   return "/member/login_cookie_need";  // templates/member/login_cookie_need.html
 }
 
  
}