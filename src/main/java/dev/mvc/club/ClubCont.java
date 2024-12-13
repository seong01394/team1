package dev.mvc.club;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/club")
public class ClubCont {
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 510;

  /** 페이징 목록 주소 */
  private String list_file_name = "/club/list_search";
  
  public ClubCont() {
    System.out.println("-> ClubCont created."); 
  }  
  
  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  @GetMapping(value="/create")
  public String create(Model model) {
    ClubVO clubVO = new ClubVO();
    model.addAttribute("clubVO", clubVO);

    return "/club/create"; // /templates/club/create.html
    }    
    
  /**
   * 등록 처리
   * 
   * @param model
   * @param clubVO
   * @return
   */
  @PostMapping(value="/create")
  public String create(Model model, @Valid
                           @ModelAttribute("clubVO") ClubVO clubVO,
                           BindingResult bindingResult) {
    
    if (bindingResult.hasErrors() == true) {

    return "/club/create"; // /templates/club/create.html
    } 

    int cnt = this.clubProc.create(clubVO);
    
    if (cnt == 1) {

    } else {
      model.addAttribute("code", "create_fail");
    }
    
   
    return "/club/msg"; // /templates/club/msg.html
  } 
  
  /**
   * 등록 폼 및 목록
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    ClubVO clubVO = new ClubVO();
    
    ArrayList<String> list_clubname = this.clubProc.clubnameset();
    clubVO.setClubname(String.join("/", list_clubname));

    model.addAttribute("clubVO", clubVO);

    ArrayList<ClubVO> list = this.clubProc.list_all();
    model.addAttribute("list", list);
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);


    return "/club/list_all"; // /templates/club/list_all.html
  }  
  
  /**
   * 조회
   */
  @GetMapping(value="/read/{clubno}")
  public String read(Model model, @PathVariable("clubno") Integer clubno,
                          @RequestParam(name="word", defaultValue="") String word,
                          @RequestParam(name="now_page", defaultValue="") int now_page) {
                   
    ClubVO clubVO = this.clubProc.read(clubno);
    model.addAttribute("clubVO", clubVO);
       
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    
    return "/club/read";
   }
                      
  /**
   * 수정폼
   */
  @GetMapping(value="/update/{clubno}")
  public String update(HttpSession session, Model model,
                             @PathVariable("clubno") Integer clubno,
                             @RequestParam(name="word", defaultValue="") String word,
                             @RequestParam(name="now_page", defaultValue="") int now_page) {
    
    return"/club/update";
  }
}
