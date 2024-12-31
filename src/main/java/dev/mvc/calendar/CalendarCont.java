package dev.mvc.calendar;

import java.util.ArrayList;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/calendar")
public class CalendarCont {
  @Autowired
  @Qualifier("dev.mvc.calendar.CalendarProc")
  private CalendarProcInter calendarProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/calendar/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  /**
   * 등록폼
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @GetMapping(value="/create")
  public String create(Model model) {
    CalendarVO calendarVO = new CalendarVO();
    model.addAttribute("calendarVO", calendarVO);
    
    return "/calendar/create";    
  }
  
  /**
   * 등록 처리
   * @param model
   * @param calendarVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value="create")
  public String create(Model model, @Valid 
                            @ModelAttribute("calendarVO")CalendarVO calendarVO,
                            BindingResult bindingResult) {
    
    if (bindingResult.hasErrors() == true) {
      
      return "/calendar/create";
    }
    
    int cnt = this.calendarProc.create(calendarVO);
    System.out.println("-> cnt:" + cnt);
    
    if (cnt == 1) {
      
      return "redirect:/calendar/list_all";
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "/calendar/msg";
  }
  
  
  /**
   * 목록
   * @param model
   * @return
   */
  @GetMapping(value="/list_all")
  public String list_all(HttpSession session, Model model,
                            @RequestParam(name="calendarno", defaultValue="") String calendarno
//                            @RequestParam(name="word", defaultValue="") String word,               
//                            @RequestParam(name="now_page", defaultValue = "1") int now_page
                            ) {
 
    CalendarVO calendarVO = new CalendarVO();
    
    model.addAttribute("calendarVO", calendarVO);
    
    ArrayList<CalendarVO> list = this.calendarProc.list_all();
    model.addAttribute("list",list);
    
//    int search_count = this.calendarProc.list_search_count(word);
//    String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
//        this.page_per_block);
//    model.addAttribute("paging", paging);
//    model.addAttribute("now_page", now_page);
//
//
//    int no = search_count - ((now_page - 1) * this.record_per_page);
//    model.addAttribute("no", no);
    
    return "/calendar/list_all";
  }
  
  /**
   * 조회
   */
  @GetMapping(value="/read")
  public String read(Model model, 
                          @RequestParam(name = "word", defaultValue = "") String word,
                          @RequestParam(name = "now_page", defaultValue = "") int now_page) {
  
   ArrayList<CalendarVO> list = this.calendarProc.list_search_paging(word, now_page, this.record_per_page);
   model.addAttribute("list", list);
   
   model.addAttribute("word", word);
   
   // --------------------------------------------------------------------------------------
   // 페이지 번호 목록 생성
   // --------------------------------------------------------------------------------------
   int search_count = this.calendarProc.list_search_count(word);
   String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
       this.page_per_block);
   model.addAttribute("paging", paging);
   model.addAttribute("now_page", now_page);

   // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
   int no = search_count - ((now_page - 1) * this.record_per_page);
   model.addAttribute("no", no);
   // --------------------------------------------------------------------------------------

   return "/calendar/read";
  }
  
  /**
   * 수정폼
   */
  @GetMapping(value="/update")
  public String update(HttpSession session, Model model,
                             @RequestParam(name = "word", defaultValue = "") String word,
                             @RequestParam(name = "now_page", defaultValue = "0") int now_page) {
    
    if(this.adminProc.isAdmin(session)) {
      ArrayList<CalendarVO> list = this.calendarProc.list_search_paging(word, now_page, this.record_per_page);
      
      model.addAttribute("word", word);
      
      int search_count = this.calendarProc.list_search_count(word);
      String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "/calendar/update"; 
    } else {
      return "redirect:/admin/login_cookie_need"; // redirect
    }
  }  
    
  /**
   * 수정 처리
   */
  @PostMapping(value="/update")
  public String update(HttpSession session, Model model, @Valid CalendarVO calendarVO, BindingResult bindingResult,
                              @RequestParam(name = "word", defaultValue = "") String word,
                              @RequestParam(name = "now_page", defaultValue = "0") int now_page,
                              RedirectAttributes ra) {
    
    if (this.adminProc.isAdmin(session)) {

      if (bindingResult.hasErrors()) { 
        return "/calendar/update";
      }

      int cnt = this.calendarProc.update(calendarVO);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {
        ra.addAttribute("word", word); 
        ra.addAttribute("now_page", now_page); 
        return "redirect:/calendar/update/";
      } else {
        model.addAttribute("code", "update_fail");
      }

      model.addAttribute("cnt", cnt);

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.calendarProc.list_search_count(word);
      String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "/calendar/msg"; 
    } else {
      return "redirect:/admin/login_cookie_need"; 
    }
  }
  
  
  /**
   * 삭제폼
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model,@PathVariable("calendarno") Integer calendarno,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name = "now_page", defaultValue = "") int now_page) {
    if (this.adminProc.isAdmin(session)) {
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);

      ArrayList<CalendarVO> list = this.calendarProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.calendarProc.list_search_count(word);
      String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "/calendar/delete"; 

    } else {
      return "redirect:/admin/login_cookie_need"; 
    }
    
  }

  /**
   * 삭제 처리
   */
  @PostMapping(value = "/delete")
  public String delete_process(HttpSession session, Model model, 
                                       @RequestParam(name = "calendarno", defaultValue = "0") Integer calendarno,
                                       @RequestParam(name = "word", defaultValue = "") String word,
                                       @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
    if (this.adminProc.isAdmin(session)) {
      
      CalendarVO calendarVO = this.calendarProc.read(calendarno);
      model.addAttribute("calendarVO", calendarVO);
      
      int cnt = this.calendarProc.delete(calendarno);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {

        ra.addAttribute("word", word); // redirect로 데이터 전송

        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.calendarProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        // ----------------------------------------------------------------------------------------------------------

        ra.addAttribute("now_page", now_page);

        return "redirect:/calendar/list_search";
      } else {
        model.addAttribute("code", "delete_fail");
      }

      model.addAttribute("cnt", cnt);

      return "/calendar/msg"; 
    } else {
      return "redirect:/admin/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 등록 폼 및 검색 목록 + 페이징 
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "calendarno", defaultValue = "0") int calendarno,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
    if (this.adminProc.isAdmin(session)) {
      CalendarVO calendarVO = new CalendarVO();
      
      model.addAttribute("calendarVO", calendarVO);
      
      word = Tool.checkNull(word);

      ArrayList<CalendarVO> list = this.calendarProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      int search_cnt = this.calendarProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);

      model.addAttribute("word", word); // 검색어


      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      int search_count = this.calendarProc.list_search_count(word);
      String paging = this.calendarProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------

      return "/calendar/list_search"; 
    } else {
      return "redirect:/admin/login_cookie_need"; 
    }
  }
  
}