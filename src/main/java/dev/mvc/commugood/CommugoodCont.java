package dev.mvc.commugood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVOMenu;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/commugood")
public class CommugoodCont {
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  @Autowired
  @Qualifier("dev.mvc.commugood.CommugoodProc")
  private CommugoodProcInter commugoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
   
  public CommugoodCont() {
    System.out.println("-> CommugoodCont created.");
  }
  
  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
                               @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }
  

  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody CommugoodVO commugoodVO) {
    System.out.println("->수신 데이터:" + commugoodVO.toString());
    
    
   // int memberno = 4; // test
    int memberno = (int)session.getAttribute("memberno"); // 보안성 향상
    commugoodVO.setMemberno(memberno);
    
    int cnt = this.commugoodProc.create(commugoodVO);
    
    JSONObject json = new JSONObject();
    json.put("res", cnt);
    
    return json.toString();    
  }
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<CommuCommugoodMemberVO> list = this.commugoodProc.list_all_join();
    model.addAttribute("list", list);

    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    return "/commugood/list_all"; 
  }
  
  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
                                   Model model, 
                                   @RequestParam(name="commugoodno", defaultValue = "0") int commugoodno, 
                                   RedirectAttributes ra) {    
    
    if (this.adminProc.isAdmin(session)) { // 관리자 로그인 확인
      this.commugoodProc.delete(commugoodno);

      return "redirect:/commugood/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/admin/login_cookie_need"); 
      return "redirect:/commugood/post2get"; 
    }
 
  }  
}
