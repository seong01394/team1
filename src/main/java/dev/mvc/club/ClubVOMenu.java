package dev.mvc.club;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ClubVOMenu {
  
  private String pl;  // 대분류
  
  private ArrayList<ClubVO> list_name;  // 중분류
}