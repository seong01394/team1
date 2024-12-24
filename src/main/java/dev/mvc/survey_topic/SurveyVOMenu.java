package dev.mvc.survey_topic;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SurveyVOMenu {
  /**  카테고리 그룹(대분류)*/
  private String topic; 
  
  /**  카테고리 그룹(중분류)*/
  private ArrayList<SurveytopicVO> list_name;
  
}
