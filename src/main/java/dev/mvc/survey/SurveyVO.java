package dev.mvc.survey;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class SurveyVO {

  /** 설문조사 번호 */
  private Integer surveyno = 0;
  
  /** 설문조사 타이틀 */
  @NotEmpty(message="타이틀은 필수")
  @Size(min=2, max=15, message="타이틀은 최소 2자에서 최대 15자입니다.")
  private String survey_title;
  /** 출력순서 */
  @NotNull(message="출력순서는 필수")
  @Min(value=0)
  @Max(value=20)
  private Integer seqno;
  /** 질문 수 */
  @NotNull(message="질문수는 필수")
  @Min(value=0)
  @Max(value=20)
  private Integer qa_num;
  /** 질문내용 */
  @NotEmpty(message="질문내용은 필수")
  @Size(min=2, max=40, message="타이틀은 최소 2자에서 최대 40자입니다.")
  private String qa_contents;
  /** 시작 날 */
  @NotEmpty(message="시작 날은 필수")
  @Size(min=2, max=40, message="시작 날은 10자.")
  private String start_date;
  /** 완료 날 */
  @NotEmpty(message="시작 날은 필수")
  @Size(min=2, max=40, message="완료 날은 10자.")
  private String fin_date;
  /** 작성일자 */
  private String write_date = "";
  
  /**선택 여부 */
  private String box_check_one = "";
  private String box_check_two = "";
  private String box_check_three = "";
  private String box_check_four = "";

}
