package dev.mvc.survey_item;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.member.MemberProcInter;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
import dev.mvc.survey_topic.Surveytopic;
import dev.mvc.survey_topic.SurveytopicProcInter;
import dev.mvc.survey_topic.SurveytopicVO;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/survey_item")
public class SurveyitemCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/th/survey_item/list_search";  
  @Autowired
  @Qualifier("dev.mvc.survey_item.SurveyitemProc")
  private SurveyitemProcInter surveyitemProc;

  @Autowired
  @Qualifier("dev.mvc.survey_topic.SurveytopicProc")
  private SurveytopicProcInter surveytopicProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @GetMapping(value = "/create")
  public String create(HttpSession session, Model model,
      @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
      @ModelAttribute("surveyitemVO") SurveyitemVO surveyitemVO) {


          SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
          model.addAttribute("surveytopicVO", surveytopicVO);

          
          return "/th/survey_item/create"; // survey/create.html로 리턴
      } 
  

    
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request,
      HttpSession session,
      Model model,
      @Valid 
      @ModelAttribute("surveyitemVO") SurveyitemVO surveyitemVO,
      @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
      BindingResult bindingResult,
      RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
    // 유효성 검사 실패 시 다시 입력 폼으로 이동
      if (bindingResult.hasErrors()) {
        bindingResult.getAllErrors().forEach(error -> {
            System.out.println("Validation Error: " + error.getDefaultMessage());
        });
        return "/th/survey_item/create"; // 에러 발생 시 다시 입력 폼으로
    }
     
    System.out.println("Received surveytopicno: " + surveyitemVO.getSurveytopicno());
    
    SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
    model.addAttribute("surveytopicVO", surveytopicVO);
    
    // 데이터베이스에 설문 데이터 저장
    surveyitemVO.setItem(surveyitemVO.getItem().trim()); // 항목 공백 제거
    int cnt = this.surveyitemProc.create(surveyitemVO);   // 항목 등록
    if (cnt == 1) {
      return "redirect:/th/survey_topic/read?" + "surveyno=" + surveytopicVO.getSurveyno();  // 설문조사 선택화면으로 이동
    } else {
      model.addAttribute("code", "create_fail"); // 등록 실패 메시지
    }

    model.addAttribute("cnt", cnt); // 결과 추가
    return "/th/survey_item/msg"; // 메시지 페이지로 이동
  } else {
    return "redirect:/th/member/login_cookie_need"; // redirect
  }
 }

    

     

    /**
     * 조회 http://localhost:9093/surveytopic/read/1
     */
    @GetMapping(value = "/read")
    public String read(Model model, 
        @RequestParam(name="surveytopicno", defaultValue = "") int surveytopicno) {
      model.addAttribute("surveytopicno", surveytopicno);
      
            
      SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
      model.addAttribute("surveytopicVO", surveytopicVO);
             
      ArrayList<SurveyitemVO> surveyitemList = this.surveyitemProc.listBySurveytopicno(surveytopicno);
      model.addAttribute("surveyitemList", surveyitemList);         
     
      System.out.println("->surveyitemList:" + surveyitemList);
      return "/th/survey_topic/read";    
    }    
    
    /**
     * 
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update")
    public String update(HttpSession session, Model model, 
                                  @RequestParam(name="surveyitemno", defaultValue = "0") int surveyitemno,
                                  @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno) {
      if (this.memberProc.isMemberAdmin(session)) {
        model.addAttribute("surveyitemno", surveyitemno);
        model.addAttribute("surveytopicno", surveytopicno); 
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);
        
        SurveyitemVO surveyitemVO = this.surveyitemProc.read(surveyitemno);
        model.addAttribute("surveyitemVO", surveyitemVO);
        

  
        return "/th/survey_item/update"; // templaes/cate/update.html
      } else {
        return "redirect:/th/member/login_cookie_need"; // redirect
      }
    }
    
    /**
     * 파일 수정 처리
     * 
     * @return
     */
    @PostMapping(value = "/update")
    public String update(HttpSession session, Model model, RedirectAttributes ra,
        @ModelAttribute("surveyitemVO") SurveyitemVO surveyitemVO,
        @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
        @RequestParam(name = "surveyitemno", defaultValue = "0") int surveyitemno){

        // 관리자 권한 체크
        if (this.memberProc.isMemberAdmin(session)) {

          
            // Survey_item 업데이트
            int cnt = this.surveyitemProc.update(surveyitemVO);
            System.out.println("-> cnt: " + cnt);

            SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
            model.addAttribute("surveytopicVO", surveytopicVO);

            return "redirect:/th/survey_topic/read?" + "surveyno=" + surveytopicVO.getSurveyno(); // 업데이트 실패 시 메시지 페이지
        } else {
            return "redirect:/th/member/login_cookie_need"; // 권한이 없을 경우 로그인 페이지로 리다이렉트
        }
    }


   
    
    /**
     * 삭제폼 http://localhost:9091/cate/delete/1
     */
    @GetMapping(value = "/delete")
    public String delete(HttpSession session, Model model, 
                                @ModelAttribute("surveyitemVO") SurveyitemVO surveyitemVO,
                                @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
                                @RequestParam(name = "surveyitemno", defaultValue = "0") int surveyitemno,
                                RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        model.addAttribute("surveytopicno", surveytopicno);
        model.addAttribute("surveyitemno", surveyitemno); 
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);
        

        return "/th/survey_item/delete"; // templaes/survey_item/delete.html
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/th/survey_item/msg";  // redirect
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
    public String delete_process(HttpSession session, Model model,    
                                           @ModelAttribute("surveyitemVO") SurveyitemVO surveyitemVO,
                                           @RequestParam(name = "surveytopicno", defaultValue = "0") Integer surveytopicno,
                                           @RequestParam(name = "surveyitemno", defaultValue = "0") Integer surveyitemno) {
      if (this.memberProc.isMemberAdmin(session)) {
        System.out.println("-> delete_process");

        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);
        
        int cnt = this.surveyitemProc.delete(surveyitemno);
        System.out.println("-> cnt: " + cnt);
       
        return "redirect:/survey_topic/read?" + "surveyno=" + surveytopicVO.getSurveyno();
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/survey_item/msg";
      }
   
    }
    

    
    /**
     * 등록 폼 및 검색 목록 + 페이징
     * @param model
     * @return
     */    
    @GetMapping(value = "/list_search")
    public String list_search_paging(HttpSession session, Model model,
        @RequestParam(name = "surveyitemno", defaultValue = "0") Integer  surveyitemno,
        @RequestParam(name = "surveytopicno", defaultValue = "0") Integer  surveytopicno,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {
     
      if (this.memberProc.isMemberAdmin(session)) {
               
        word = Tool.checkNull(word);        
        
        ArrayList<SurveytopicSurveyitemVO> list = this.surveyitemProc.list_search_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);   
        
        int search_cnt = this.surveyitemProc.list_search_count(word);
        model.addAttribute("search_cnt", search_cnt);

        model.addAttribute("word", word); // 검색어

        int search_count = this.surveyitemProc.list_search_count(word);
        String paging = this.surveyitemProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);

        return "/th/survey_item/list_search"; 
      } else {
        return "redirect:/th/member/login_cookie_need";

      }

 }  
}
