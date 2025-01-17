package dev.mvc.news;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.admin.AdminProcInter;
import dev.mvc.club.ClubVO;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RequestMapping(value = "/news")
@Controller
public class NewsCont{
  @Autowired
  @Qualifier("dev.mvc.admin.AdminProc")
  private AdminProcInter adminProc;
  
  @Autowired
  @Qualifier("dev.mvc.news.NewsProc")
  private NewsProcInter newsProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 10;

  /** 페이징 목록 주소 */
  private String list_file_name = "/th/news/list_search";
  
  public NewsCont() {
    System.out.println("-> NewsCont created.");
  }
  
  @GetMapping(value = "/create")
  public String create(HttpSession session,Model model) {

    if(this.adminProc.isAdmin(session)) {
      NewsVO newsVO = new NewsVO();
      model.addAttribute("newsVO", newsVO);
      
      return "/th/news/create";
    }else {
      return "redirect:/th/admin/login_cookie_need";      
    } 
  }
  
  @PostMapping(value="/create")
  public String create(HttpSession session, Model model, @Valid
                           @ModelAttribute("newsVO") NewsVO newsVO,
                           BindingResult bindingResult,
                           RedirectAttributes ra) {
         
    if (bindingResult.hasErrors() == true) {

    return "/thnews/create"; 
    } 
    
    int cnt = this.newsProc.create(newsVO);
   
    if (cnt == 1) {

      return "redirect:/th/news/list_search"; 
    } else {
      model.addAttribute("code", "create_fail");
    }
    
    model.addAttribute("cnt", cnt);
    
    return "news/msg"; 
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
                           @RequestParam(name="newsno", defaultValue="") String newsno,
                           @RequestParam(name="now_page", defaultValue = "1") int now_page) {
                                                 
   NewsVO newsVO = new NewsVO();
    
    model.addAttribute("newsVO", newsVO);

    ArrayList<NewsVO> list = this.newsProc.list_all();
    model.addAttribute("list", list);
    
    int search_count = this.newsProc.list_search_count(word);
    String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);

    return "/th/news/list_all";
  }  
  
  /**
   * 조회
   */
  @GetMapping(value="/read/{newsno}")
  public String read(Model model, @PathVariable("newsno") Integer newsno,
                          @RequestParam(name="word", defaultValue="") String word,
                          @RequestParam(name="now_page", defaultValue="1") int now_page) {
                   
    NewsVO newsVO = this.newsProc.read(newsno);
    
    model.addAttribute("newsVO", newsVO);
     
    model.addAttribute("word", word);
    
    int search_count = this.newsProc.list_search_count(word);
    String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    
    return "/th/news/read";
   }
  
  /**
   * 등록 폼 및 검색 목록 + 페이징
   * @param model
   * @return
   */
  @GetMapping(value = "/list_search")
  public String list_search_paging(HttpSession session, Model model,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "newsno", defaultValue = "0") int newsno,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page,
      RedirectAttributes ra) {
   
    if (this.adminProc.isAdmin(session)) {
      NewsVO newsVO = new NewsVO();
      model.addAttribute("newsVO", newsVO);
      
      word = Tool.checkNull(word);

      ArrayList<NewsVO> list = this.newsProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      int search_cnt = this.newsProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);

      model.addAttribute("word", word); // 검색어

      int search_count = this.newsProc.list_search_count(word);
      String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);

      return "/th/news/list_search"; 
    } else {
      return "redirect:/th/admin/login_cookie_need";
    }
  }
  
  /**
   * 수정폼
   */
  @GetMapping(value="/update/{newsno}")
  public String update(HttpSession session, Model model,
                             @PathVariable("newsno") Integer newsno,
                             @RequestParam(name="word", defaultValue="") String word,
                             @RequestParam(name="now_page", defaultValue="1") int now_page) {
    
    if(this.adminProc.isAdmin(session)) {
      NewsVO newsVO = this.newsProc.read(newsno);
      model.addAttribute("newsVO", newsVO);
      
      ArrayList<NewsVO> list =this.newsProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      model.addAttribute("word", word);
      
      int search_count = this.newsProc.list_search_count(word);
      String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block); 
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "/th/news/update"; 
    } else {
      return "redirect:/th/admin/login_cookie_need"; // redirect
    }
   } 
  
  /**
   * 수정 처리
   * @return
   */
  @PostMapping(value = "/update")
  public String update(HttpSession session, Model model, @Valid 
                              @ModelAttribute("newsVO") NewsVO newsVO, BindingResult bindingResult,
                              @RequestParam(name = "word", defaultValue = "") String word,
                              @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                              RedirectAttributes ra) {
  
    if (this.adminProc.isAdmin(session)) {
    if (bindingResult.hasErrors() == true) { 

      return "/th/news/update"; 
    }
    
    int cnt = this.newsProc.update(newsVO);
    System.out.println("-> cnt: " + cnt);

    if (cnt == 1) {

      ra.addAttribute("word", word); 
      ra.addAttribute("now_page", now_page); 

      ra.addAttribute("newsno", newsVO.getNewsno());
      return "redirect:/th/news/read/" + newsVO.getNewsno();
    } else {
      model.addAttribute("code", "update_fail");
    }

    model.addAttribute("cnt", cnt);

    int search_count = this.newsProc.list_search_count(word);
    String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
        this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);

    return "redirect:/th/news/read";
    } else {
      return "redirect:/th/admin/login_cookie_need"; 
    }  
  }
  
  
  @GetMapping(value = "/delete/{newsno}")
  public String delete(HttpSession session, Model model, 
                            @PathVariable("newsno") Integer newsno,
                            @RequestParam(name = "word", defaultValue = "") String word,
                            @RequestParam(name = "now_page", defaultValue = "1") int now_page,
                            RedirectAttributes ra) {
    if (this.adminProc.isAdmin(session)) {
      NewsVO newsVO = this.newsProc.read(newsno);
      model.addAttribute("newsVO", newsVO);
      
      ArrayList<NewsVO> list = this.newsProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);

      int search_count = this.newsProc.list_search_count(word);
      String paging = this.newsProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page,
          this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);

      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);

      return "/th/news/delete"; 

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
                                       @RequestParam(name = "newsno", defaultValue = "0") Integer newsno,
                                       @RequestParam(name = "word", defaultValue = "") String word,
                                       @RequestParam(name = "now_page", defaultValue = "1") int now_page, 
                                       RedirectAttributes ra) {
    if (this.adminProc.isAdmin(session)) {

      NewsVO newsVO = this.newsProc.read(newsno); 
      model.addAttribute("newsVO", newsVO);
      
      int cnt = this.newsProc.delete(newsno);
      System.out.println("-> cnt: " + cnt);

      if (cnt == 1) {

        ra.addAttribute("word", word); // redirect로 데이터 전송

        int search_cnt = this.newsProc.list_search_count(word);
        if (search_cnt % this.record_per_page == 0) {
          now_page = now_page - 1;
          if (now_page < 1) {
            now_page = 1;
          }
        }

        ra.addAttribute("now_page", now_page); 

        return "redirect:/th/news/list_search";
      } else {
        model.addAttribute("code", "delete_fail");
      }

      model.addAttribute("cnt", cnt);

      return "/th/club/msg";
    } else {
      return "redirect:/th/admin/login_cookie_need";  
    }

  }
 
}