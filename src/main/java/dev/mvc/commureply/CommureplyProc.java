package dev.mvc.commureply;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Tool;

@Component("dev.mvc.commureply.CommureplyProc")
public class CommureplyProc implements CommureplyProcInter {
  @Autowired
  CommureplyDAOInter commureplyDAO;
  
  @Override
  public int create(CommureplyVO commreplyVO) {
    int cnt = this.commureplyDAO.create(commreplyVO);
    return cnt;
  }
  
  @Override
  public List<CommureplyMemberVO> list_by_communo_join(int communo) {
    List<CommureplyMemberVO> list = this.commureplyDAO.list_by_communo_join(communo);
    return list;
  }
}