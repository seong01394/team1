package dev.mvc.surveymember;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.surveymember.SurveymemberProc")
public class SurveymemberProc implements SurveymemberProcInter {

  @Autowired
  SurveymemberDAOInter surveymemberDAO;
  @Override
  public ArrayList<SurveytopicitemmemberVO> list_all_join() {
    ArrayList<SurveytopicitemmemberVO> list = this.surveymemberDAO.list_all_join();
    return list;
  }

}
