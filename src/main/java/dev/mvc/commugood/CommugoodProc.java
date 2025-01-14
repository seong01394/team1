package dev.mvc.commugood;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("dev.mvc.commugood.CommugoodProc")
public class CommugoodProc implements CommugoodProcInter {
  @Autowired
  CommugoodDAOInter commugoodDAO;
  
  @Override
  public int create(CommugoodVO commugoodVO) {
    int cnt = this.commugoodDAO.create(commugoodVO);
    
    return cnt;
  }
  
  @Override
  public ArrayList<CommugoodVO>list_all(){
    ArrayList<CommugoodVO> list = this.commugoodDAO.list_all();
    
    return list;
  }
  
  @Override
  public int delete(int commugoodno) {
    int cnt = this.commugoodDAO.delete(commugoodno);
    
    return cnt;
  }

  @Override
  public int heartCnt(HashMap<String, Object> map) {
    int cnt = this.commugoodDAO.heartCnt(map);
    return cnt;
  }

  @Override
  public CommugoodVO read(int commugoodno) {
    CommugoodVO commugoodVO = this.commugoodDAO.read(commugoodno);
    return commugoodVO;
  }
  
  @Override
  public CommugoodVO readByCommunoMemberno(HashMap<String, Object> map) {
    CommugoodVO commugoodVO = this.commugoodDAO.readByCommunoMemberno(map);
    return commugoodVO;
  }

  @Override
  public ArrayList<CommuCommugoodMemberVO> list_all_join() {
    ArrayList<CommuCommugoodMemberVO> list = this.commugoodDAO.list_all_join();
    
    return list;
  }
  
 
}
