package dev.mvc.surveymember;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping(value = "/surveymember")
public class SurveymemberCont {

  @Autowired
  @Qualifier("dev.mvc.surveymember.SurveymemberProc") 
  SurveymemberProcInter surveymemberProc;

  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @GetMapping(value = "/list_all")
  public String list_all(Model model, HttpSession session) {
    
      if (this.memberProc.isMemberAdmin(session)) {
      ArrayList<SurveytopicitemmemberVO> list = this.surveymemberProc.list_all_join();
      model.addAttribute("list", list);
      
      System.out.println("list:" + list);
    
      return "/th/surveymember/list_all"; // /templates/calendar/list_all.html
    } else {
      return "redirect:/th/member/login_cookie_need"; // 로그인 안되어 있으면 리다이렉트
    }
  }
  
  
  
}
