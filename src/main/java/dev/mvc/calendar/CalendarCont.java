package dev.mvc.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/calendar")
public class CalendarCont {
  @Autowired
  @Qualifier("dev.mvc.calendar.CalendarProc")
  private CalendarProcInter calendarProc;
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
    
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
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
  
  
  /**
   * 등록폼
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @GetMapping(value="/create")
  public String create(Model model) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    return "/th/calendar/create";    
  }
  
  /**
   * 등록 처리
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpSession session, Model model, 
           @ModelAttribute("calendarVO") CalendarVO calendarVO) {
    
    int adminno = 1; // 테스트용
    calendarVO.setAdminno(adminno);
    
    int cnt = this.calendarProc.create(calendarVO);

    if (cnt == 1) {
      // model.addAttribute("code", "create_success");
      // model.addAttribute("name", cateVO.getName());

      return "redirect:/th/calendar/list_all"; // @GetMapping(value="/list_all")
    } else {
      model.addAttribute("code", "create_fail");
    }

    model.addAttribute("cnt", cnt);

    return "/th/calendar/msg"; 
  }
  
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ArrayList<CalendarVO> list = this.calendarProc.list_all();
    model.addAttribute("list", list);

    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    return "/th/calendar/list_all"; // /templates/calendar/list_all.html
  }
  
  /**
   * 조회
   * 
   * @return
   */
  @GetMapping(path = "/read/{calendarno}")
  public String read(Model model, @PathVariable("calendarno") int calendarno) {
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    this.calendarProc.increaseCnt(calendarno); // 조회수 증가
    
    CalendarVO calendarVO = this.calendarProc.read(calendarno);

    model.addAttribute("calendarVO", calendarVO);

    return "/th/calendar/read";
  }
    
  /**
   * 수정 폼
   *
   */
  @GetMapping(value = "/update")
  public String update_text(HttpSession session, 
      Model model, 
      @RequestParam(name="calendarno", defaultValue = "0") int calendarno, 
      RedirectAttributes ra) {    
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    if (this.adminProc.isAdmin(session)) { // 관리자로 로그인한경우
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);

      return "/th/calendar/update"; // /templates/calendar/update.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/th/admin/login_cookie_need"; 
    }

  }
    
  /**
   * 수정 처리
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, 
      Model model, 
      @ModelAttribute("calendarVO") CalendarVO calendarVO, 
      RedirectAttributes ra) {
    
    if (this.adminProc.isAdmin(session)) { // 관리자 로그인 확인
      this.calendarProc.update(calendarVO); // 글수정

      return "redirect:/th/calendar/read/" + calendarVO.getCalendarno(); // @GetMapping(value = "/read")

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/admin/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendar/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
  
  /**
   * 삭제폼
   */
  @GetMapping(path = "/delete/{calendarno}")
  public String delete(HttpSession session, 
      Model model, 
      @PathVariable("calendarno") int calendarno, 
      RedirectAttributes ra) {    
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    if (this.adminProc.isAdmin(session)) {
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);

      return "/th/calendar/delete"; // /templates/calendar/delete.html
    } else {
      // ra.addAttribute("url", "/member/login_cookie_need"); // /templates/member/login_cookie_need.html
      // return "redirect:/contents/msg"; // @GetMapping(value = "/msg")
      return "/th/admin/login_cookie_need"; 
    }
  }

  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String delete_proc(HttpSession session, 
      Model model, 
      @RequestParam(name="calendarno", defaultValue = "0") int calendarno, 
      RedirectAttributes ra) {    
    
    if (this.adminProc.isAdmin(session)) { // 관리자 로그인 확인
      this.calendarProc.delete(calendarno);

      return "redirect:/th/calendar/list_all";

    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/th/admin/login_cookie_need"); // /templates/member/login_cookie_need.html
      return "redirect:/calendar/post2get"; // @GetMapping(value = "/msg")
    }

  }
  
  /**
   * 특정 날짜의 목록
   * 현재 월: http://localhost:9091/calendar/list_calendar
   * 이전 월: http://localhost:9091/calendar/list_calendar?year=2024&month=12 
   * 다음 월: http://localhost:9091/calendar/list_calendar?year=2024&month=1
   * @param model
   * @return
   */
  @GetMapping(value = "/list_calendar")
  public String list_calendar(Model model,
      @RequestParam(name="year", defaultValue = "0") int year,
      @RequestParam(name="month", defaultValue = "0") int month) {
    
    if (year == 0) {
        // 현재 날짜를 가져옴
        LocalDate today = LocalDate.now();

        // 년도와 월 추출
        year = today.getYear();
        month = today.getMonthValue();
    } 
    
    String month_str = String.format("%02d", month); // 두 자리 형식으로
    System.out.println("-> month: " + month_str);
  
    String date = year + "-" + month;
    System.out.println("-> date: " + date);
    
//    ArrayList<CalendarVO> list = this.calendarProc.list_calendar(date);
//    model.addAttribute("list", list);

    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    
    model.addAttribute("year", year);
    model.addAttribute("month", month-1);  // javascript는 1월이 0임. 
    
    return "/th/calendar/list_calendar"; // /templates/calendar/list_calendar.html
  }
  
  /**
   * 특정 날짜의 목록
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_calendar_day")
  @ResponseBody
  public String list_calendar_day(Model model, @RequestParam(name="labeldate", defaultValue = "") String labeldate) {
  System.out.println("-> labeldate: " + labeldate);
  
    ArrayList<CalendarVO> list = this.calendarProc.list_calendar_day(labeldate);
    model.addAttribute("list", list);

    JSONArray schedule_list = new JSONArray();
    
    for (CalendarVO calendarVO: list) {
        JSONObject schedual = new JSONObject();
        schedual.put("calendarno", calendarVO.getCalendarno());
        schedual.put("labeldate", calendarVO.getLabeldate());
        schedual.put("label", calendarVO.getLabel());
        schedual.put("seqno", calendarVO.getSeqno());
        
        schedule_list.put(schedual);
    }

    return schedule_list.toString();
    
  }
  
  /**
   * 우선 순위 높임, 10 등 -> 1 등,
   */
  @GetMapping(value = "/update_seqno_forward/{calendarno}")
  public String update_seqno_forward(Model model, @PathVariable("calendarno") Integer calendarno, RedirectAttributes ra) {
    this.calendarProc.update_seqno_forward(calendarno);

    return "redirect:/th/calendar/list_all"; 
  }

  /**
   * 우선 순위 낮춤, 1 등 -> 10 등
   */
  @GetMapping(value = "/update_seqno_backward/{calendarno}")
  public String update_seqno_backward(Model model, @PathVariable("calendarno") Integer calendarno, RedirectAttributes ra) {
    this.calendarProc.update_seqno_backward(calendarno);

    return "redirect:/th/calendar/list_all";
  }
  
}