package dev.mvc.club;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("dev.mvc.club.ClubProc")
public class ClubProc implements ClubProcInter {
  @Autowired
  private ClubDAOInter clubDAO;
  
  public ClubProc() {
    System.out.println("-> ClubProc created");
  }
  
  @Override
  public int create(ClubVO clubVO) {
    int cnt = this.clubDAO.create(clubVO);
    
    return cnt;
  }
  
  @Override
  public ArrayList<ClubVO> list_all() {
    ArrayList<ClubVO>list = this.clubDAO.list_all();
    
    return list;
  }
  
  @Override
  public ClubVO read(Integer clubno) {
    ClubVO clubVO = this.clubDAO.read(clubno);
    
    return clubVO;
  }

  @Override
  public int update(ClubVO clubVO) {
    int cnt = this.clubDAO.update(clubVO);
    
    return cnt;
  }

  @Override
  public int delete(int clubno) {
    int cnt = this.clubDAO.delete(clubno);
    
    return cnt;
  }
  
  @Override
  public int update_rank_up(int clubno) {
    int cnt = this.clubDAO.update_rank_up(clubno);
    
    return cnt;
  }
  
  @Override
  public int update_rank_down(int clubno) {
    int cnt = this.clubDAO.update_rank_down(clubno);
    
    return cnt;
  }  
  
  @Override
  public ArrayList<ClubVOMenu> menu() {
    // 대분류+중분류의 결합 목록
    ArrayList<ClubVOMenu> menu = new ArrayList<ClubVOMenu>();
    
    ArrayList<ClubVO> clubname = this.clubDAO.list_all_clubgrp_y(); // 대분류 목록 추출
    for (ClubVO clubVO:clubname) {
      ClubVOMenu clubVOMenu = new ClubVOMenu(); // 대분류+중분류의 결합 객체
      
      // System.out.println(cateVO.getGenre());
      clubVOMenu.setClubname(clubVO.getClubname()); // 대분류명 저장
      
      // 카테고리 그룹(대분류)에 해당하는 카테고리 목록(중분류) 로딩
      ArrayList<ClubVO> list_name = this.clubDAO.list_all_club_y(clubVO.getClubname());
      clubVOMenu.setList_name(list_name);
      
      menu.add(clubVOMenu);
    }
    
    return menu;
  }

  @Override
  public ArrayList<String> clubnameset() {
    ArrayList<String> list = this.clubDAO.clubnameset();
    
    return list;
  }

  @Override
  public ArrayList<ClubVO> list_all_clubgrp_y() {
    ArrayList<ClubVO> list = this.clubDAO.list_all_clubgrp_y();
    
    return list;
  }

  @Override
  public ArrayList<ClubVO> list_all_club_y(String clubname) {
    ArrayList<ClubVO> list = this.clubDAO.list_all_club_y(clubname);
    
    return list;
  }

}