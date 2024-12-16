package dev.mvc.survey;

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


import dev.mvc.member.MemberProcInter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/survey")
public class SurveyCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/survey/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc") // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  // // http://localhost:9091/survey/create
    @GetMapping(value = "/create")
    public String create(HttpSession session, Model model) {
     if (this.memberProc.isMemberAdmin(session)) {
        SurveyVO surveyVO = new SurveyVO();
        model.addAttribute("surveyVO", surveyVO);
        
        return "/survey/create";
      } else {
        return "redirect:/member/login_cookie_need";
      }
    }
    
  /**
  * 등록 처리, http://localhost:9093/cate/create
  */ 
    @PostMapping(value = "/create")
    public String create(Model model, 
                                @Valid
                                @ModelAttribute("surveyVO") SurveyVO surveyVO, 
                                BindingResult bindingResult) {
      if (bindingResult.hasErrors() == true) {
        return "/survey/create";
       }
      
      int cnt = this.surveyProc.create(surveyVO);
      System.out.println("-> cnt: " + cnt);
      
      if (cnt == 1) {
        return "redirect:/survey/list_search";
      } else {
        model.addAttribute("cnt", cnt);
      }
      
      model.addAttribute("cnt", cnt);
      
      return "/survey/msg";
    }
    
    /**
     * 등록 폼 및 목록
     * http://localhost:9093/survey/list_all
     * @param model
     * @return
     */
    @GetMapping(value = "/list_all")
    public String list_all(Model model) {
      SurveyVO surveyVO = new SurveyVO();
      model.addAttribute("surveyVO", surveyVO);
      
      ArrayList<SurveyVO> list = this.surveyProc.list_all();
      model.addAttribute("list", list);
      
      return "/return/list_all";
    }
    /**
     * 조회 http://localhost:9093/survey/read/1
     */
    @GetMapping(value = "/read/{surveyno}")
    public String read(Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
      
      String paging = this.surveyProc.pagingBox(now_page, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      return "/survey/read";    
    }
    
    /**
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update/{surveyno}")
    public String update(HttpSession session, Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      if (this.memberProc.isMemberAdmin(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);

        String paging = this.surveyProc.pagingBox(now_page, this.record_per_page, this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        return "/survey/update"; // templaes/cate/update.html
      } else {
        return "redirect:/member/login_cookie_need"; // redirect
      }
    }
    
    /**
     * 수정 처리, http://localhost:9091/cate/update
     * 
     * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
     * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
     *                      자동 실행
     * @param bindingResult 폼에 에러가 있는지 검사 지원
     * @return
     */
    @PostMapping(value = "/update")
    public String update(HttpSession session, Model model, @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO, BindingResult bindingResult,
        @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        
        if (bindingResult.hasErrors() == true) { // 에러가 있으면 폼으로 돌아갈 것.
          return "/cate/update"; // /templates/cate/update.html
        }

//      System.out.println(cateVO.getName());
//      System.out.println(cateVO.getSeqno());
//      System.out.println(cateVO.getVisible());

      int cnt = this.surveyProc.update(surveyVO);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송

        return "redirect:/survey/update/" + surveyVO.getSurveyno(); // @GetMapping(value="/update/{cateno}")
      } else {
        model.addAttribute("code", "update_fail");
      }

      model.addAttribute("cnt", cnt);

      // --------------------------------------------------------------------------------------
      // 페이지 번호 목록 생성
      // --------------------------------------------------------------------------------------
      String paging = this.surveyProc.pagingBox(now_page, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      return "/survey/msg"; // /templates/cate/msg.html
      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }
      
    }
    
    /**
     * 삭제폼 http://localhost:9091/cate/delete/1
     */
    @GetMapping(value = "/delete/{surveyno}")
    public String delete(HttpSession session, Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      if (this.memberProc.isMemberAdmin(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        model.addAttribute("now_page", now_page);

        // --------------------------------------------------------------------------------------
        // 페이지 번호 목록 생성
        // --------------------------------------------------------------------------------------
        String paging = this.surveyProc.pagingBox(now_page, this.record_per_page, this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);


        return "/survey/delete"; // templaes/cate/delete.html

      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }
      
    }

    /**
     * 삭제 처리, http://localhost:9091/cate/delete?cateno=1
     * 
     * @param model         Controller -> Thymeleaf HTML로 데이터 전송에 사용
     * @param cateVO        Form 태그 값 -> 검증 -> cateVO 자동 저장, request.getParameter()
     *                      자동 실행
     * @param bindingResult 폼에 에러가 있는지 검사 지원
     * @return
     */
    @PostMapping(value = "/delete")
    public String delete_process(HttpSession session, Model model, @RequestParam(name = "surveyno", defaultValue = "0") Integer surveyno,
        @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        System.out.println("-> delete_process");

        SurveyVO surveyVO = this.surveyProc.read(surveyno); // 삭제전에 삭제 결과를 출력할 레코드 조회
        model.addAttribute("surveyVO", surveyVO);
        
        
        int cnt = this.surveyProc.delete(surveyno);
        System.out.println("-> cnt: " + cnt);

        return "/cate/msg"; // /templates/cate/msg.html
      } else {
        return "redirect:/member/login_cookie_need";  // redirect
      }

    }
}
