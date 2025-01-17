package dev.mvc.club;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
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
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/th/club/list_search";
  
  public ClubCont() {
    System.out.println("-> ClubCont created."); 
  }  
  
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  /**
   * 등록 폼
   * 
   * @param model
   * @return
   */
  @GetMapping(value="/create")
  public String create(HttpSession session,Model model) {
    
    if(this.adminProc.isAdmin(session)) {
    
    ClubVO clubVO = new ClubVO();
    model.addAttribute("clubVO", clubVO);

    return "/th/club/create";
    } else{
      return "redirect:/th/admin/login_cookie_need";
    }
 }
    
  /**
   * 등록 처리
   * 
   * @param model
   * @param clubVO
   * @return
   */
  @PostMapping(value="/create")
  public String create(HttpSession session, Model model, @Valid
                           @ModelAttribute("clubVO") ClubVO clubVO,
                           BindingResult bindingResult,
                           RedirectAttributes ra) {
    if (adminProc.isAdmin(session)) { 

      String emblem = ""; 
      String emblemsaved = "";

      String upDir = Club.getUploadDir();
      System.out.println("-> upDir: " + upDir);

      MultipartFile mf = clubVO.getFile1MF();

      emblem = mf.getOriginalFilename(); 
      System.out.println("-> 원본 파일명 산출 emblem: " + emblem);

      long size1 = mf.getSize(); 
      if (size1 > 0) { 
        if (Tool.checkUploadFile(emblem) == true) { 
          emblemsaved = Upload.saveFileSpring(mf, upDir);
         
          clubVO.setEmblem(emblem); 
          clubVO.setEmblem(emblemsaved); 

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); 
          ra.addFlashAttribute("cnt", 0);
          ra.addFlashAttribute("url", "/club/msg"); 
          return "redirect:/club/msg"; 
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }
    }
          
    if (bindingResult.hasErrors() == true) {

    return "/th/club/create"; 
    } 

    clubVO.setPl(clubVO.getPl().trim());
    clubVO.setClubname(clubVO.getClubname().trim());
    int cnt = this.clubProc.create(clubVO);
    
    if (cnt == 1) {

      return "redirect:/th/club/list_search"; 
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
   
    return "/th/club/msg"; // /templates/club/msg.html
  } 
  
  /**
   * 목록
   * 
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model,
                           @RequestParam(name="word", defaultValue="") String word,
                           @RequestParam(name="clubno", defaultValue="") String clubno,
                           @RequestParam(name="now_page", defaultValue = "1") int now_page) {
                                                 
    ClubVO clubVO = new ClubVO();
    
    ArrayList<String> list_pl = this.clubProc.plset();
    clubVO.setPl(String.join("/", list_pl));

    model.addAttribute("clubVO", clubVO);

    ArrayList<ClubVO> list = this.clubProc.list_all();
    model.addAttribute("list", list);
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    int search_count = this.clubProc.list_search_count(word);
    String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);


    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);

    return "/th/club/list_all"; // /templates/club/list_all.html
  }  
  
  /**
   * 조회
   */
  @GetMapping(value="/read/{clubno}")
  public String read(Model model, @PathVariable("clubno") Integer clubno,
                          @RequestParam(name="word", defaultValue="") String word,
                          @RequestParam(name="now_page", defaultValue="1") int now_page) {
                   
    ClubVO clubVO = this.clubProc.read(clubno);
    model.addAttribute("clubVO", clubVO);
       
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    
    int search_count = this.clubProc.list_search_count(word);
    String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    
    return "/th/club/read";
   }
                      
  /**
   * 수정폼
   */
  @GetMapping(value="/update/{clubno}")
  public String update(HttpSession session, Model model,
                             @PathVariable("clubno") Integer clubno,
                             @RequestParam(name="word", defaultValue="") String word,
                             @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    if(this.adminProc.isAdmin(session)) {
      ClubVO clubVO = this.clubProc.read(clubno);
      model.addAttribute("clubVO", clubVO);
      
      ArrayList<ClubVO> list =this.clubProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      ArrayList<ClubVOMenu> menu = this.clubProc.menu();
      model.addAttribute("menu", menu);
      
      ArrayList<String> list_pl = this.clubProc.plset();
      model.addAttribute("list_pl", String.join("/", list_pl));

      model.addAttribute("word", word);
      
      int search_count = this.clubProc.list_search_count(word);
      String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "/th/club/update"; 
    } else {
      return "redirect:/th/member/admin_login"; // redirect
    }
   } 
  
  /**
   * 수정 처리
   * @return
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, @Valid 
                              @ModelAttribute("clubVO") ClubVO clubVO, BindingResult bindingResult,
                              @RequestParam(name = "word", defaultValue = "") String word,
                              @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                              RedirectAttributes ra) {
  
    if (this.adminProc.isAdmin(session)) {
    if (bindingResult.hasErrors() == true) { 

      return "/th/club/update"; 
    }
    
    int cnt = this.clubProc.update(clubVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {

      ra.addAttribute("word", word); 
      ra.addAttribute("now_page", now_page); 

      ra.addAttribute("clubno", clubVO.getClubno());
      return "redirect:/th/club/read/" + clubVO.getClubno();
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    int search_count = this.clubProc.list_search_count(word);
    String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);

    return "redirect:/th/club/read";
    } else {
      return "redirect:/th/admin/login_cookie_need"; 
    }  
  }
  
  @GetMapping(value = "/delete/{clubno}")
  public String delete(HttpSession session, Model model, 
                            @PathVariable("clubno") Integer clubno,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                            RedirectAttributes ra) {
    if (this.adminProc.isAdmin(session)) {
      ClubVO clubVO = this.clubProc.read(clubno);
      model.addAttribute("clubVO", clubVO);
      
      ArrayList<ClubVO> list = this.clubProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      ArrayList<ClubVOMenu> menu = this.clubProc.menu();
      model.addAttribute("menu", menu);

      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      int search_count = this.clubProc.list_search_count(word);
      String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);

      return "/th/club/delete"; 

    } else {
      return "redirect:/th/admin/login_cookie_need";  
    }   
  }  
  
  /**
   * 삭제 처리
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete_process(HttpSession session, Model model, 
                                       @RequestParam(name = "clubno", defaultValue = "0") Integer clubno,
                                       @RequestParam(name = "word", defaultValue = "") String word,
                                       @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                       RedirectAttributes ra) {
    if (this.adminProc.isAdmin(session)) {

      ClubVO clubVO = this.clubProc.read(clubno); 
      model.addAttribute("clubVO", clubVO);
      
      int cnt = this.clubProc.delete(clubno);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {

        ra.addAttribute("word", word); // redirect로 데이터 전송

        int search_cnt = this.clubProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1;
          }
        }

        ra.addAttribute("now_page", now_page); 

        return "redirect:/th/club/list_search";
      } else {
        model.addAttribute("code", "delete_fail");
      }

      model.addAttribute("cnt", cnt);

      return "/th/club/msg";
    } else {
      return "redirect:/th/admin/login_cookie_need";  
    }

  }

  /**
   * 구단 순위 높임, 10 등 -> 1 등
   */
  @GetMapping(value = "/update_rank_up/{clubno}")
  public String update_rank_up(Model model, 
                                         @PathVariable("clubno") Integer clubno,
                                         @RequestParam(name = "word", defaultValue = "") String word,
                                         @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                         RedirectAttributes ra) {
    
    this.clubProc.update_rank_up(clubno);

    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    return "redirect:/th/club/list_search";
  }  
 
  /**
   * 구단 순위 낮춤, 1 등 -> 10 등
   */
  @GetMapping(value = "/update_rank_down/{clubno}")
  public String update_rank_down(Model model, 
                                             @PathVariable("clubno") Integer clubno,
                                             @RequestParam(name = "word", defaultValue = "") String word,
                                             @RequestParam(name = "now_page", defaultValue = "") int now_page, 
                                             RedirectAttributes ra) {
    
    this.clubProc.update_rank_down(clubno);

    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page); 

    return "redirect:/th/club/list_search"; 
  }  

  /**
   * 등록 폼 및 검색 목록 + 페이징
   * @param model
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "clubno", defaultValue = "0") int clubno,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page,
      RedirectAttributes ra) {
   
    if (this.adminProc.isAdmin(session)) {
      ClubVO clubVO = new ClubVO();

      ArrayList<String> list_pl = this.clubProc.plset();
      clubVO.setPl(String.join("/", list_pl));
      
      model.addAttribute("clubVO", clubVO);
      
      word = Tool.checkNull(word);

      ArrayList<ClubVO> list = this.clubProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      ArrayList<ClubVOMenu> menu = this.clubProc.menu();
      model.addAttribute("menu", menu);

      int search_cnt = this.clubProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);

      model.addAttribute("word", word); // 검색어

      int search_count = this.clubProc.list_search_count(word);
      String paging = this.clubProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);

      return "/th/club/list_search"; 
    } else {
      return "redirect:/member/admin_login";
    }
  }
  
// @GetMapping(value = "/update_emblem")
// public String update_emblem(HttpSession session, Model model,
//                                        @RequestParam(name="clubno", defaultValue="")int clubno,
//                                        @RequestParam(name="word", defaultValue = "") String word, 
//                                        @RequestParam(name="now_page", defaultValue = "1") int now_page) {
//   
//   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
//   model.addAttribute("menu", menu);
//   
//   model.addAttribute("word", word);
//   model.addAttribute("now_page", now_page);
//   
//   ClubVO clubVO = this.clubProc.read(clubno);
//   model.addAttribute("clubVO", clubVO);
//
//   clubVO = this.clubProc.read(clubVO.getClubno());
//   model.addAttribute("clubVO", clubVO);
//
//   return "/club/update_emblem";
//   
// }
 
}
