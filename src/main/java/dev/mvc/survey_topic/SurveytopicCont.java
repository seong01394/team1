package dev.mvc.survey_topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;


import dev.mvc.member.MemberProcInter;
import dev.mvc.survey.SurveyProcInter;
import dev.mvc.survey.SurveyVO;
import dev.mvc.survey_item.SurveyitemProcInter;
import dev.mvc.survey_item.SurveyitemVO;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/survey_topic")
public class SurveytopicCont {
  
  @Autowired
  @Qualifier("dev.mvc.survey_topic.SurveytopicProc")
  private SurveytopicProcInter surveytopicProc;

  @Autowired
  @Qualifier("dev.mvc.survey_item.SurveyitemProc")
  private SurveyitemProcInter surveyitemProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.survey.SurveyProc")
  private SurveyProcInter surveyproc;
  
  
  @GetMapping(value = "/create")
  public String create(HttpSession session, Model model,
      @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
      @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO) {


          SurveyVO surveyVO = this.surveyproc.read(surveyno);
          model.addAttribute("surveyVO", surveyVO);

          
          return "/survey_topic/create"; // survey/create.html로 리턴
      } 
  

    
  @PostMapping(value = "/create")
  public String create(
      HttpServletRequest request,
      HttpSession session,
      Model model,
      @Valid @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO,
      @RequestParam(name = "surveyno", defaultValue = "") int surveyno,
      BindingResult bindingResult,
      RedirectAttributes ra) {
    if (this.memberProc.isMemberAdmin(session)) {
    // 유효성 검사 실패 시 다시 입력 폼으로 이동
    if (bindingResult.hasErrors()) {
      return "/survey_topic/create"; // templates/survey/create.html
    }
    System.out.println("Received surveyno: " + surveytopicVO.getSurveyno());
    
    // 파일 업로드 처리
    String filesaved = "";   // 저장된 파일명
    String filethumb = "";       // 축소판 파일명
    String file1 = "";
    
    String upDir = Surveytopic.getUploadDir(); // 업로드 디렉토리 경로 (Survey 클래스에 정의된 메서드 가정)
    MultipartFile mf = surveytopicVO.getFile1MF(); // MultipartFile 객체 가져오기
    
    file1 = mf.getOriginalFilename();
    long filesize = mf.getSize();             // 파일 크기

    if (filesize > 0) { // 파일이 업로드된 경우
      if (Tool.checkUploadFile(file1)) { // 파일 형식 검사
        filesaved = Upload.saveFileSpring(mf, upDir); // 파일 저장
        if (Tool.isImage(filesaved)) { // 이미지 파일인지 검사
          filethumb = Tool.preview(upDir, filesaved, 200, 150); // 축소판 생성
        }
        surveytopicVO.setFile1(file1); // 원본 이미지 저장
        surveytopicVO.setFilesaved(filesaved); // 저장된 파일명 저장
        surveytopicVO.setFilethumb(filethumb);    // 축소판 파일명 저장
        surveytopicVO.setFilesize(filesize);       // 파일 크기 저장
      } else {
        ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 실패 메시지
        return "redirect:/survey_topic/msg"; // 업로드 실패 메시지 페이지로 리다이렉트
      }
    }

    // 데이터베이스에 설문 데이터 저장
    surveytopicVO.setTopic(surveytopicVO.getTopic().trim()); // 제목 공백 제거
    int cnt = this.surveytopicProc.create(surveytopicVO);   // 개별문제 등록
    if (cnt == 1) {
      return "redirect:/survey_topic/read?" + "surveyno=" +surveytopicVO.getSurveyno(); //8 설문 목록으로 이동
    } else {
      model.addAttribute("code", "create_fail"); // 등록 실패 메시지
    }

    model.addAttribute("cnt", cnt); // 결과 추가
    return "/survey_topic/msg"; // 메시지 페이지로 이동
  } else {
    return "redirect:/member/login_cookie_need"; // redirect
  }
 }

    

     

    /**
     * 조회 http://localhost:9093/surveytopic/read/1
     */
    @GetMapping(value = "/read")
    public String read(Model model,
        @RequestParam(name="surveyno", defaultValue = "") Integer surveyno) {
      
      
      
      SurveyVO surveyVO = this.surveyproc.read(surveyno);
      model.addAttribute("surveyVO", surveyVO);
                  
      ArrayList<SurveytopicVO> surveytopicList = this.surveytopicProc.listBySurveyno(surveyno);
      model.addAttribute("surveytopicList", surveytopicList);
      
       // 각 주제에 대한 항목을 저장할 맵
       Map<Integer, ArrayList<SurveyitemVO>> itemsByTopic = new HashMap<>();

          // 각 설문 주제를 반복하면서 해당 설문 주제 번호로 항목 조회
          for (SurveytopicVO topic : surveytopicList) {
              ArrayList<SurveyitemVO> surveyitemList = this.surveyitemProc.listBySurveytopicno(topic.getSurveytopicno());
              itemsByTopic.put(topic.getSurveytopicno(), surveyitemList); // 주제 번호를 키로 하고 항목 리스트를 값으로 저장
              System.out.println("Items for topic " + topic.getSurveytopicno() + ": " + surveyitemList.size());
          }
          
          model.addAttribute("itemsByTopic", itemsByTopic);
          
      System.out.println("-> surveytopicList:" + surveytopicList );
      
      return "/survey_topic/read";    
    }    
    
    /**
     * 
     * 수정폼 http://localhost:9093/survey/update/1
     */
    @GetMapping(value = "/update")
    public String update(HttpSession session, Model model, 
                                  @RequestParam(name="surveytopicno", defaultValue = "0") int surveytopicno,
                                  @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {
      if (this.memberProc.isMemberAdmin(session)) {
        model.addAttribute("surveytopicno", surveytopicno);
        model.addAttribute("surveyno", surveyno); 
        
        SurveyVO surveyVO = this.surveyproc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);
        

  
        return "/survey_topic/update"; // templaes/cate/update.html
      } else {
        return "redirect:/member/login_cookie_need"; // redirect
      }
    }
    
    /**
     * 파일 수정 처리
     * 
     * @return
     */
    @PostMapping(value = "/update")
    public String update(HttpSession session, Model model, RedirectAttributes ra,
        @ModelAttribute("surveytopicVO") SurveytopicVO surveytopicVO,
        @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
        @RequestParam(name = "surveyno", defaultValue = "0") int surveyno) {

        // 관리자 권한 체크
        if (this.memberProc.isMemberAdmin(session)) {

          
            // Survey 업데이트
            int cnt = this.surveytopicProc.update(surveytopicVO);
            System.out.println("-> cnt: " + cnt);



            return "redirect:/survey_topic/read?" + "surveyno=" + surveytopicVO.getSurveyno(); // 업데이트 실패 시 메시지 페이지
        } else {
            return "redirect:/member/login_cookie_need"; // 권한이 없을 경우 로그인 페이지로 리다이렉트
        }
    }


   
    
    /**
     * 삭제폼 http://localhost:9091/cate/delete/1
     */
    @GetMapping(value = "/delete")
    public String delete(HttpSession session, Model model, 
                                @RequestParam(name = "surveytopicno", defaultValue = "0") int surveytopicno,
                                @RequestParam(name = "surveyno", defaultValue = "0") int surveyno,
                                RedirectAttributes ra) {
      if (this.memberProc.isMemberAdmin(session)) {
        model.addAttribute("surveytopicno", surveytopicno);
        model.addAttribute("surveyno", surveyno); 
        
        SurveyVO surveyVO = this.surveyproc.read(surveyno);
        model.addAttribute("surveyVO", surveyVO);
        
        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno);
        model.addAttribute("surveytopicVO", surveytopicVO);

        return "/survey_topic/delete"; // templaes/cate/delete.html
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/survey_topic/msg";  // redirect
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
                                           @RequestParam(name = "surveytopicno", defaultValue = "0") Integer surveytopicno,
                                            @RequestParam(name = "surveyno", defaultValue = "0") Integer surveyno) {
      if (this.memberProc.isMemberAdmin(session)) {
        System.out.println("-> delete_process");

        SurveytopicVO surveytopicVO = this.surveytopicProc.read(surveytopicno); // 삭제전에 삭제 결과를 출력할 레코드 조회
        model.addAttribute("surveytopicVO", surveytopicVO);
        
        
        int cnt = this.surveytopicProc.delete(surveytopicno);
        System.out.println("-> cnt: " + cnt);
       
        return "redirect:/survey_topic/read?" + "surveyno=" + surveytopicVO.getSurveyno();
        
      } else {
        model.addAttribute("code", "delete_fail");
        return "redirect:/survey_topic/msg";
      }
   
    }

 }  


    
    


 
