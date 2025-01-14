package dev.mvc.survey_topic;

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

@Setter @Getter @ToString
public class SurveySurveytopicVO {

  /** 설문조사(개별문제)번호 */
  private Integer surveytopicno= 0;
  /** 설문조사 번호 */
  private Integer surveyno= 0;
  
  /** 설문조사 제목 */
  private String survey_title;
  
  /** 제목 */
  @NotEmpty(message="제목은 필수")
  @Size(min=2, max=200, message="제목은 최소 2자에서 최대 15자입니다.")
  private String topic;
  /** 출력 순서 */
  @NotNull(message="출력순서 필수")
  @Size(min=1, max=10, message="출력순서 필수")
  private String seqno;

  
  
  



}
