package dev.mvc.surveygood;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SurveygoodVO {
  
  /** 설문조사 참여 번호*/
  private Integer surveygoodno;
  
  /** 참여 날짜*/
  private String rdate;
  
  /** 설문조사 번호*/
  private Integer surveyno;
  
  /** 회원 번호*/
  private Integer memberno;
  
  
}
