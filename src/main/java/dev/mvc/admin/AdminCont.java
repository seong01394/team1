package dev.mvc.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.club.ClubProcInter;

@RequestMapping("/admin")
@Controller
public class AdminCont {
  @Autowired
  @Qualifier("dev.mvc.club.AdminProc")
  private AdminProcInter adminProc;  
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  public AdminCont() {
    System.out.println("-> AdminCont created");
  }
  
  @GetMapping(value="/checkID")
  @ResponseBody
  public String checkID(@RequestParam(name="id", defaultValue="") String id) {
    
    int cnt = this.adminProc.checkID(id);
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  
}