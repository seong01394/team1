package dev.mvc.survey;

import java.util.ArrayList;
import java.util.HashMap;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;



import dev.mvc.member.MemberProcInter;
import dev.mvc.survey_item.SurveyitemProcInter;
import dev.mvc.survey_item.SurveyitemVO;
import dev.mvc.survey_topic.SurveytopicProcInter;
import dev.mvc.survey_topic.SurveytopicVO;
import dev.mvc.surveygood.SurveygoodProcInter;
import dev.mvc.surveygood.SurveygoodVO;
import dev.mvc.club.ClubVO;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/survey")
public class SurveyCont {
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 5;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/th/survey/list_search";
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey_topic.SurveytopicProc")
  private SurveytopicProcInter surveytopicProc; // 개별 문제를 관리하는 인터페이스 주입
  
  @Autowired
  @Qualifier("dev.mvc.survey_item.SurveyitemProc")
  private SurveyitemProcInter surveyitemProc;
  
  @Autowired
  @Qualifier("dev.mvc.surveygood.SurveygoodProc")
  private SurveygoodProcInter surveygoodProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @GetMapping(value = "/create")
  public String create(HttpSession session, Model model,
      @RequestParam(name="word", defaultValue = "") String word,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @RequestParam(name="now_page", defaultValue = "1") int now_page) {

      if (this.memberProc.isMemberAdmin(session)) {
          SurveyVO surveyVO = new SurveyVO();
          model.addAttribute("surveyVO", surveyVO);
          
          // 페이지당 항목 수를 설정 (예: 10개)
          int recordPerPage = 10; // 원하는 개수로 조정
          
          // 리스트를 가져올 때 페이지 번호와 페이지당 항목 수를 넘겨줌
          ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, recordPerPage);
          model.addAttribute("list", list);
          
          // 페이지네이션 처리
          int search_count = this.surveyProc.list_search_count(word); // 검색 결과 수
          String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, recordPerPage, this.page_per_block);
          model.addAttribute("paging", paging);
          
          return "/th/survey/create"; // survey/create.html로 리턴
      } else {
          return "redirect:/th/admin/login"; // 로그인 안되어 있으면 리다이렉트
      }
  }

    
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request,
      HttpSession session,
      Model model,
      @Valid @ModelAttribute("surveyVO") SurveyVO surveyVO,
      BindingResult bindingResult,
      RedirectAttributes ra) {

    // 유효성 검사 실패 시 다시 입력 폼으로 이동
    if (bindingResult.hasErrors()) {
      return "/th/survey/create"; // templates/survey/create.html
    }

    // 파일 업로드 처리
    String poster = "";        // 원본 파일명
    String postersaved = "";   // 저장된 파일명
    String posterthumb = "";       // 축소판 파일명
   
    String upDir = Surveys.getUploadDir(); // 업로드 디렉토리 경로 (Survey 클래스에 정의된 메서드 가정)
    MultipartFile mf = surveyVO.getFile1MF(); // MultipartFile 객체 가져오기

    poster = mf.getOriginalFilename(); // 원본 파일명
    long postersize = mf.getSize();             // 파일 크기

    if (postersize > 0) { // 파일이 업로드된 경우
      if (Tool.checkUploadFile(poster)) { // 파일 형식 검사
        postersaved = Upload.saveFileSpring(mf, upDir); // 파일 저장
        if (Tool.isImage(postersaved)) { // 이미지 파일인지 검사
          posterthumb = Tool.preview(upDir, postersaved, 200, 150); // 축소판 생성
        }
        surveyVO.setPoster(poster);       // 원본 파일명 저장
        surveyVO.setPostersaved(postersaved); // 저장된 파일명 저장
        surveyVO.setPosterthumb(posterthumb);     // 축소판 파일명 저장
        surveyVO.setPostersize(postersize);       // 파일 크기 저장
      } else {
        ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 실패 메시지
        return "redirect:/th/survey/msg"; // 업로드 실패 메시지 페이지로 리다이렉트
      }
    }

    // 데이터베이스에 설문 데이터 저장
    surveyVO.setSurvey_title(surveyVO.getSurvey_title().trim()); // 제목 공백 제거
    int cnt = this.surveyProc.create(surveyVO);   // 설문 등록
    if (cnt == 1) {
      return "redirect:/th/survey/list_search"; //8 설문 목록으로 이동
    } else {
      model.addAttribute("code", "create_fail"); // 등록 실패 메시지
    }

    model.addAttribute("cnt", cnt); // 결과 추가
    model.addAttribute("surveyVO", surveyVO); // 이미지 경로 포함
    return "/th/survey/msg"; // 메시지 페이지로 이동
  }


    
    /**
     * 등록 폼 및 목록
     * http://localhost:9093/survey/list_all
     * @param model
     * @return
     */
    @GetMapping(value = "/list_all")
    public String list_all(Model model,
                                @RequestParam(name="word", defaultValue = "") String word,
                                @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    
      SurveyVO surveyVO = new SurveyVO();
      model.addAttribute("surveyVO", surveyVO);
      
      ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, now_page);
      model.addAttribute("list", list);
      
      
      model.addAttribute("word", word);
      
      // 페이지 번호 목록 생성
      int search_count = this.surveyProc.list_search_count(word);
      String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      // --------------------------------------------------------------------------------------      
     
      return "/th/survey/list_all";
      } 
      
    
    

    /**
     * 조회 http://localhost:9093/survey/read/1
     */
    @GetMapping(value = "/read/{surveyno}")
    public String read(Model model,
                                    @PathVariable("surveyno") Integer surveyno,
                                    @RequestParam(name = "surveytopicno", defaultValue = "0") Integer surveytopicno) {
      SurveyVO surveyVO = this.surveyProc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
                            
      
      this.surveyProc.increaseCnt(surveyno);
//      this.surveyitemProc.increaseitemCnt(surveyitemno);
      
      return "/th/survey_topic/read" + surveyVO.getSurveyno();    
    }    
    
    /**
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update/{surveyno}")
    public String update(HttpSession session, Model model, 
                                  @RequestParam(name="word", defaultValue = "") String word,
                                  @PathVariable("surveyno") Integer surveyno,
                                  @RequestParam(name = "now_page", defaultValue = "") int now_page) {
      if (this.memberProc.isMemberAdmin(session)) {
        SurveyVO surveyVO = this.surveyProc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        
      //ArrayList<MovieVO> list = this.moviecateProc.list_all();
        ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);
        
//      ArrayList<MovieVO> menu = this.moviecateProc.list_all_categrp_y();
//      model.addAttribute("menu", menu);
      
       
        
     
        
        // 페이지 번호 목록 생성
        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        return "/th/survey/update"; // templaes/cate/update.html
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
        @ModelAttribute("surveyVO") SurveyVO surveyVO,
        @RequestParam(name = "word", defaultValue = "") String word, 
        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

        // 관리자 권한 체크
        if (this.memberProc.isMemberAdmin(session)) {

            // Survey 업데이트
            int cnt = this.surveyProc.update(surveyVO);
            System.out.println("-> cnt: " + cnt);

            if (cnt == 1) {
                ra.addAttribute("now_page", now_page); // 리다이렉트로 페이지 정보 전달
                return "redirect:/th/survey/list_search"; // 업데이트된 survey 페이지로 리다이렉트
            } else {
                model.addAttribute("code", "update_fail"); // 업데이트 실패 처리
            }

            model.addAttribute("cnt", cnt);

            // 페이지 번호 목록 생성
            int search_count = this.surveyProc.list_search_count(word);
            String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
                this.page_per_block);
            model.addAttribute("paging", paging);
            model.addAttribute("now_page", now_page);

            return "/th/survey/msg"; // 업데이트 실패 시 메시지 페이지
        } else {
            return "redirect:/th/admin/login"; // 권한이 없을 경우 로그인 페이지로 리다이렉트
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
       
        ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);
        
        model.addAttribute("word", word);
        model.addAttribute("now_page", now_page);

        // 페이지 번호 목록 생성
        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);
        
        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);

        return "/th/survey/delete"; // templaes/cate/delete.html

      } else {
        return "redirect:/th/member/login_cookie_need";  // redirect
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
        @RequestParam(name="word", defaultValue="") String word,
        @RequestParam(name = "now_page", defaultValue = "") int now_page, RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        System.out.println("-> delete_process");

        SurveyVO surveyVO = this.surveyProc.read(surveyno); // 삭제전에 삭제 결과를 출력할 레코드 조회
        model.addAttribute("surveyVO", surveyVO);
        
        
        int cnt = this.surveyProc.delete(surveyno);
        System.out.println("-> cnt: " + cnt);

        if (cnt ==1) {
//        model.addAttribute("code", "delete_success");
//        model.addAttribute("genre", movieVO.getGenre());
//        model.addAttribute("name", movieVO.getName());
        ra.addAttribute("word", word); // redirect로 데이터 전송
        
        
        // ----------------------------------------------------------------------------------------------------------
        // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
        int search_cnt = this.surveyProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1; // 최소 시작 페이지
          }
        }
        
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
        
        return "redirect:/th/survey/list_search";
        
      } else {
        model.addAttribute("code", "delete_fail");
      }
      
      model.addAttribute("cnt", cnt);
      
      return "/th/survey/msg"; //templates/cate/msg.html
    } else {
      return "redirect:/th/member/admin_login";  // redirect
    }
    }
    
    /**
     * 등록 폼 및 검색 목록 + 페이징
     * @param model
     * @return
     */
    @GetMapping(value = "/list_search")
    public String list_search_paging(HttpSession session, Model model,
        @RequestParam(name = "word", defaultValue = "") String word,
        @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
        @RequestParam(name = "now_page", defaultValue = "1") int now_page,
        @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO) {
     
      if (this.memberProc.isMember(session)) {
        SurveyVO surveyVO = new SurveyVO();

        
        model.addAttribute("surveyVO", surveyVO);
        
        word = Tool.checkNull(word);

        ArrayList<SurveyVO> list = this.surveyProc.list_paging(word, now_page, this.record_per_page);
        model.addAttribute("list", list);


        int search_cnt = this.surveyProc.list_search_count(word);
        model.addAttribute("search_cnt", search_cnt);

        model.addAttribute("word", word); // 검색어

        int search_count = this.surveyProc.list_search_count(word);
        String paging = this.surveyProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
            this.page_per_block);
        model.addAttribute("paging", paging);
        model.addAttribute("now_page", now_page);

        int no = search_count - ((now_page - 1) * this.record_per_page);
        model.addAttribute("no", no);
        
        ArrayList<SurveytopicVO> surveytopicList = surveytopicProc.listBySurveyno(surveyno); // 또는 적절한 메서드 호출
        model.addAttribute("surveytopicList", surveytopicList);
        
        

        
        return "/th/survey/list_search"; 
      } else {
        return "/th/member/login_cookie_need";
      }
    }
      
    
    /**
     *   <!-- 카테고리 공개 설정 -->,  http://localhost:9091/cate/update_visible_y/1
     * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
     * @return
     */
    @GetMapping(value="/update_visible_y/{surveyno}")
    public String update_visible_y(HttpSession session, Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name="word", defaultValue="") String word,
        @RequestParam(name="now_page", defaultValue = "") int now_page,
        RedirectAttributes ra ) {
      
      if (this.memberProc.isMemberAdmin(session)) {
        this.surveyProc.update_y_n_y(surveyno);
        
        ra.addAttribute("word", word); // redirect로 데이터 전송
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
        
        return "redirect:/th/survey/list_search"; // @GetMapping(value="/list_all")
      
      } else {
        return "redirect:/th/member/login_cookie_need";  // redirect
      }
      
    }
    
    /**
     *   <!-- 카테고리 비공개 설정 -->,  http://localhost:9091/cate/update_visible_n/1
     * @param model Controller -> Thymleaf HTML로 데이터 전송에 사용
     * @return
     */
    @GetMapping(value="/update_visible_n/{surveyno}")
    public String update_visible_n(HttpSession session, Model model, @PathVariable("surveyno") Integer surveyno,
        @RequestParam(name="word", defaultValue="") String word,
        @RequestParam(name="now_page", defaultValue = "") int now_page,
        RedirectAttributes ra ) {
      
      if (this.memberProc.isMemberAdmin(session)) {
        this.surveyProc.update_y_n_n(surveyno);
        
        ra.addAttribute("word", word); // redirect로 데이터 전송
        ra.addAttribute("now_page", now_page); // redirect로 데이터 전송
        
        return "redirect:/th/survey/list_search"; // @GetMapping(value="/list_all")
      } else {
        return "redirect:/th/member/login_cookie_need";  // redirect
      }
    }
      /**
       * 수정처리 http://localhost:9093/survey/good
       */
      @PostMapping(value="/good")
      @ResponseBody
      public String good(HttpSession session, Model model, @RequestBody String json_src) {
        System.out.println("->json_src:" + json_src);
        
        JSONObject src = new JSONObject(json_src); // String -> JSON
        int surveyno = (int)src.get("surveyno"); // 값 가져오기
        System.out.println("-> surveyno: " + surveyno);       
        
        if(this.memberProc.isMember(session)) {
          // 추천을 한 상태인지 확인
          int memberno = (int)session.getAttribute("memberno");
          HashMap<String, Object> map = new HashMap<String, Object>();         
          map.put("surveyno", surveyno);
          map.put("memberno", memberno);
          
          int good_cnt = this.surveygoodProc.hartCnt(map);
          System.out.println("-> good_cnt:" + good_cnt);
          
          
          
          if (good_cnt == 1) {
            // 추천 해제
            System.out.println("-> 추천 해제:" + ' '+ surveyno + ' ' + memberno);
            
            SurveygoodVO surveygoodVO = this.surveygoodProc.readBySurveynoMemberno(map);
            
            this.surveygoodProc.delete(surveygoodVO.getSurveygoodno()); // 좋아요 삭제
            this.surveyProc.decreaseRecom(surveyno); // 카운트 감소
          } else {
            //추천
            System.out.println("-> 추천:" + surveyno + ' ' + memberno);
            SurveygoodVO surveygoodVO_new = new SurveygoodVO();
            surveygoodVO_new.setSurveyno(surveyno);
            surveygoodVO_new.setMemberno(memberno);
            
            this.surveygoodProc.create(surveygoodVO_new);
            this.surveyProc.increaseRecom(surveyno);
          }
          //추천 여부가 변경되어 다시 새로운 값을 읽어옴
          int hartCnt = this.surveygoodProc.hartCnt(map);
          int recom = this.surveyProc.read(surveyno).getRecom();
          
          JSONObject result = new JSONObject();
          result.put("isMember", 1); // 로그인 1, 비회원: 0
          result.put("hartCnt", hartCnt);
          result.put("recom", recom);
          
          System.out.println("->result.toString():" + result.toString());
          return result.toString();

        } else {
          JSONObject result = new JSONObject();          
          result.put("isMember", 0); // 로그인 1, 비회원: 0
          System.out.println("->result.toString():" + result.toString());
          return result.toString();
        }
      
      }     
      
}
