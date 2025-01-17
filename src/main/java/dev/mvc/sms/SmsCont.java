package dev.mvc.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.member.MemberProcInter;


@Controller
@RequestMapping("/sms")
public class SmsCont {
  public SmsCont() {
    System.out.println("-> SMSCont created.");
  }
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;

  // http://localhost:9093/sms/form
  /**
   * 사용자의 전화번호 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/form")
  public String form(Model model, HttpSession session) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    // ID 찾은 경우 어느 회원의 패스워드를 변경하는지 확인할 목적으로 id를 session 에 저장
    session.setAttribute("id", "user1");
    
    return "/th/sms/form";
  }
  
  /**
   * 비밀번호 찾기에서 가입 유무 확인 버튼 클릭 시 db에서 검증
   * @param id
   * @param msg
   * @return
   */
  @GetMapping(value="/isExist") // http://localhost:9093/sms/isExist?id=id&rphone=rphone
  @ResponseBody
  public String isExist(HttpSession session,
      @RequestParam(name="id", defaultValue = "")String id,
      @RequestParam(name="rnickname", defaultValue = "")String rnickname) {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("id", id);
    map.put("rnickname", rnickname);
    
    System.out.println("-> id: " + id);
    System.out.println("-> rnickname: " + rnickname);
    
    int cnt = this.memberProc.passwd_check_find(map);
    JSONObject obj = new JSONObject();
    
    if(cnt == 1) {  // 아이디와 닉네임가 일치하는 회원이 있을 시 세션에 입력받은 ID 저장
      session.setAttribute("authenticatedID", id);
    }
    
    obj.put("cnt", cnt);
    
    return obj.toString();
  }

  // http://localhost:9093/sms/proc
  /**
   * 사용자에게 인증 번호를 생성하여 전송
   * 
   * @param session
   * @param request
   * @return
   */
  @PostMapping(value = "/proc")
  public ModelAndView proc(HttpSession session, HttpServletRequest request) {
    ModelAndView mav = new ModelAndView();

    // ------------------------------------------------------------------------------------------------------
    // 0 ~ 9, 번호 6자리 생성
    // ------------------------------------------------------------------------------------------------------
    String auth_no = "";
    Random random = new Random();
    for (int i = 0; i <= 5; i++) {
      auth_no = auth_no + random.nextInt(10); // 0 ~ 9, 번호 6자리 생성
    }
    session.setAttribute("auth_no", auth_no); // 생성된 번호를 비교를위하여 session에 저장
    // System.out.println(auth_no);
    // ------------------------------------------------------------------------------------------------------

    System.out.println("-> IP:" + request.getRemoteAddr()); // 접속자의 IP 수집

    // 번호, 전화 번호, ip, auth_no, 날짜 -> SMS Oracle table 등록, 문자 전송 내역 관리 목적으로 저장(필수 아니나
    // 권장)

    String msg = "[www.Plguide.kr] [" + auth_no + "]을 인증번호란에 입력해주세요.";
    System.out.print(msg);

    mav.addObject("msg", msg); // request.setAttribute("msg")
    mav.setViewName("sms/proc"); // /WEB-INF/views/sms/proc.jsp

    return mav;
  }

  // http://localhost:9093/sms/proc_next
  /**
   * 사용자가 수신받은 인증번호 입력 화면
   * 
   * @return
   */
  @GetMapping(value = "/proc_next")
  public String proc_next(Model model, HttpSession session) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    return "/th/sms/proc_next";
  }
  
  
  @GetMapping(value="/isAuth")
  @ResponseBody
  public String isAuth(HttpSession session,
                       @RequestParam(name="inputAuthNo", defaultValue = "")String inputAuthNo) {
    String auth_no = (String)(session.getAttribute("auth_no"));
    JSONObject obj = new JSONObject();
    
    if(inputAuthNo.equals(auth_no)) {
      obj.put("result", "success");
    }
    else {
      obj.put("result", "fail");
    }
    
    return obj.toString();
  }
  
  /**
   * 인증 완료된 후 비밀번호 변경 폼
   * @param model
   * @return
   */
  @GetMapping(value="/update_passwd_find")
  public String update_passwd_find_form(Model model) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    return "/th/sms/update_passwd_find";
  }
  
  /**
   * 인증 완료된 후 비밀번호 변경 처리
   * @param session 사용자당 할당된 서버의 메모리
   * @return
   */
  @PostMapping(value = "/update_passwd_find")
  public String update_passwd_find_proc(Model model, HttpSession session,
                                        @RequestParam(name="passwd", defaultValue = "")String passwd) {
    
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("id", (String)session.getAttribute("authenticatedID"));
    map.put("passwd", passwd);
    
    JSONObject obj = new JSONObject();
    
    int cnt = this.memberProc.passwd_update_find(map);
    System.out.println("->cnt:" + cnt);
    obj.put("cnt", cnt);
    
    return obj.toString();  
  }

}
