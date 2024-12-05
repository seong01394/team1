package dev.mvc.team1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeCont {

  @GetMapping(value = {"/", "/index.do"})
  public String Home() {
    return "index";  // /templates/index.html
  }
  
}
  
  






