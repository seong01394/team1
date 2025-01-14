package dev.mvc.survey_item;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SurveytopicSurveyitemVO {
  //설문조사 항목 번호
  private Integer surveyno;
  //설문조사 항목 번호
  private Integer surveytopicno;
  //항목 내용
  private String topic;
  //설문조사 항목 번호
  private Integer surveyitemno;
  //항목 내용
  private String item;
  //항목 출력순서
  private Integer itemseq;
  //선택 인원 수
  private Integer itemcnt;
  
  
  
  
  
}
