package dev.mvc.club;

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

//CREATE TABLE club (
//    CLUBNO        NUMBER(10)        NOT NULL  PRIMARY KEY,
//    ADMINNO       NUMBER(10)        NOT NULL,  
//    CLUBNAME      VARCHAR(30)       NOT NULL,
//    PLAYER        VARCHAR(30)       NOT NULL,
//    HEADCOACH     VARCHAR(30)       NOT NULL,
//    LEGEND        VARCHAR(30)       NOT NULL,
//    HISTORY       CLOB              NOT NULL,
//    INFO          VARCHAR(255)      NOT NULL,
//    EMBLEM        VARCHAR(300)      NULL,
//    EMBLEMSAVED   VARCHAR(300)      NULL,  
//    RANK          NUMBER(5)         DEFAULT 1   NOT NULL,
//    visible       CHAR(1)           DEFAULT 'Y' NOT NULL,
//    PL            VARCHAR(100)  NOT NULL,
//    FOREIGN KEY (ADMINNO) REFERENCES admin (ADMINNO)
//  );

@Setter @Getter @ToString
public class ClubVO {
  /** 구단 번호 */  
  private Integer clubno;
  
  /** 관리자 번호 */  
  private Integer adminno = 1;

  /** 구단명 */
  @NotEmpty(message="구단명 필수 항목입니다.")
  @Size(min=2, max=15, message="구단명은 최소 2자에서 최대 10자입니다.")
  private String clubname = "";

  /** 구단 주요 선수 */
  @NotEmpty(message="선수 이름 입력은 필수 항목입니다.")
  @Size(min=2, max=12, message="선수 이름은 최소 2자에서 최대 12자입니다.")
  private String player;
  
  /** 구단 감독 */
  @NotEmpty(message="감독 이름 입력은 필수 항목입니다.")
  @Size(min=2, max=20, message="감독 이름은 최소 2자에서 최대 12자입니다.")
  private String headcoach;
  
  /** 구단 레전드 */
  @NotEmpty(message="레전드 이름 입력은 필수 항목입니다.")
  @Size(min=2, max=20, message="레전드 이름은 최소 2자에서 최대 12자입니다.")
  private String legend;
  
  /** 구단 역사  */
  private String history = "";
  
  /** 구단 정보  */
  private String info = "";
  
  /** 출력 순서 */
  @NotNull(message="출력 순서는 필수 입력 항목입니다.")
  @Min(value=1)
  @Max(value=1000000)  
  private Integer rank;
  
  /** 출력 모드 */
  @NotEmpty(message="출력 모드는 필수 항목입니다.")
  @Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
  private String visible;
  
  /** 프리미엄 리그(대분류)*/
  private String pl;
  
  /** 이미지 파일*/
  private MultipartFile file1MF = null;
  
  /** 메인 이미지 크기 단위, 파일 크기 */
  private String size1_label = "";
  
  /** 구단 엠블럼 이미지 */
  private String emblem = "";
  
  /** 실제 저장된 메인 엠블럼 이미지 */
  private String emblemsaved = "";
  
}
