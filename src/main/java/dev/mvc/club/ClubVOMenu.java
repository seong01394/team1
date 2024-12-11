package dev.mvc.club;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ClubVOMenu {
  
  private String clubname;
  
  private ArrayList<ClubVO> list_name;
}