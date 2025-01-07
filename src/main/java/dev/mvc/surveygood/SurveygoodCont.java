package dev.mvc.surveygood;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  

  public SurveygoodCont(){
    System.out.println("SurveygoodCont 만들어짐");
  }
  
  @PostMapping(value = "/create")
  public String create(HttpSession session, @RequestBody SurveygoodVO surveygoodVO) {
          
      System.out.println("-> 수신 데이터: " + surveygoodVO.toString());
      
      int memberno = (int)session.getAttribute("memberno");
      surveygoodVO.setMemberno(memberno);
      
      int cnt = this.surveygoodProc.create(surveygoodVO);
      
      JSONObject json = new JSONObject();
      json.put("res", cnt);
  
            
      return json.toString();
  } 
}
