package dev.mvc.surveygood;

import java.util.ArrayList;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.calendar.CalendarVO;
import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
import dev.mvc.survey_topic.SurveytopicVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/surveygood")
public class SurveygoodCont {

  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  private SurveygoodProcInter surveygoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  public SurveygoodCont(){
    System.out.println("SurveygoodCont 만들어짐");
  }
  
  @PostMapping(value = "/create")
  @ResponseBody
  public String create(HttpSession session, @RequestBody SurveygoodVO surveygoodVO) {
          
      System.out.println("-> 수신 데이터: " + surveygoodVO.toString());
      
//      int memberno = (int)session.getAttribute("memberno");
      int memberno = 4;
      surveygoodVO.setMemberno(memberno);
      
      int cnt = this.surveygoodProc.create(surveygoodVO);
      
      JSONObject json = new JSONObject();
      json.put("res", cnt);
  
            
      return json.toString();
  } 
  
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    
    ArrayList<SurveygoodVO> list = this.surveygoodProc.list_all();
    model.addAttribute("list", list);


    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    
    return "/surveygood/list_all";
  }
  
  @GetMapping(value = "/join_list_all")
  public String join_list_all(Model model) {
    
    ArrayList<SurveySurveygoodMemberVO> list = this.surveygoodProc.join_list_all();
    model.addAttribute("list", list);


    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    
    return "/surveygood/join_list_all";
  }
  @PostMapping(path = "/delete")
  public String delete_proc(HttpSession session, 
                                      Model model, 
                                      @RequestParam(name = "surveygoodno", defaultValue = "0") int surveygoodno, 
                                      RedirectAttributes ra) {    
    if (this.adminProc.isAdmin(session)) {
      this.surveygoodProc.delete(surveygoodno);
      
      return "/surveygood/delete"; // /templates/calendar/delete.html
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/surveygood/post2get";
    }

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

}
