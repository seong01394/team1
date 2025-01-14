package dev.mvc.surveygood;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


//select g.surveygoodno, g.rdate, g.surveyno, g.memberno, s.survey_title, m.id, m.name
//FROM survey s, surveygood g, member m
//WHERE s.surveyno = g.surveyno AND g.memberno = m.memberno
//ORDER BY surveygoodno DESC;

@Setter @Getter @ToString
public class SurveySurveygoodMemberVO {

  /** 설문조사 번호 */
  private Integer surveyno;
  
  /** 설문조사 제목 */
  private String s_survey_title;

  /** 설문조사 추천 번호*/
  private Integer surveygoodno;

  /** 회원 번호*/
  private Integer memberno;
  
  /** 참여 날짜*/
  private String rdate;
  
  /** 회원 성명 */
  private String name = "";
  
  /** 아이디(이메일) */
  private String id = "";



  
  
  



}
