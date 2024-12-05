package dev.mvc.survey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SurveyVO {

  /** 설문조사 번호 */
  private Integer surveyno; 
  /** 설문조사 타이틀 */
  private String survey_title;
  /** 출력순서 */
  private Integer seqno;
  /** 질문 수 */
  private Integer qa_num;
  /** 질문내용 */
  private String qa_contents;
  /** 시작 날 */
  private String start_date;
  /** 완료 날 */
  private String fin_date;
  /** 작성일자 */
  private String write_date = "";
}
