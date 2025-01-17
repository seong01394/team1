package dev.mvc.team1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCont {

  @GetMapping(value = {"/", "/th/index.do"})
  public String Home() {
    return "/th/index";  // /templates/index.html
  }
  
}
  
  






