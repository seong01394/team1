package dev.mvc.commu;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.club.ClubProcInter;
import dev.mvc.club.ClubVO;
import dev.mvc.club.ClubVOMenu;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;


@RequestMapping(value="/commu")
@Controller
public class CommuCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.club.ClubProc")
  private ClubProcInter clubProc;
  
  @Autowired
  @Qualifier("dev.mvc.commu.CommuProc")
  private CommuProcInter commuProc;
  
  public CommuCont() {
    System.out.println("-> CommuCont created");
  }
  
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
      @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    return url; 
  }
  
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("commuVO") CommuVO commuVO, 
      @RequestParam(name="clubno", defaultValue="0") int clubno) {
    
    ArrayList<ClubVOMenu> menu = this.clubProc.menu();
    model.addAttribute("menu", menu);

    ClubVO clubVO = this.clubProc.read(clubno); 
    model.addAttribute("clubVO", clubVO);

    return "/commu/create"; 
  }
  
 @PostMapping(value = "/create")
 public String create(HttpServletRequest request,
                            HttpSession session, Model model,
                            @ModelAttribute("commuVO") CommuVO commuVO,
                            @RequestParam(name="clubno", defaultValue="")int clubno,
                            RedirectAttributes ra) {
    
    if (memberProc.isMember(session)) { 
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String image = ""; 
      String imagesaved = ""; 
      String commuthumb = ""; 

      String upDir = Commu.getUploadDir();
      System.out.println("-> upDir: " + upDir);
      
      MultipartFile mf = commuVO.getFile1MF();

      image = mf.getOriginalFilename(); 
      System.out.println("-> 원본 파일명 산출 image: " + image);

      long imagesize = mf.getSize(); // 파일 크기
      
      if (imagesize > 0) {
        if (Tool.checkUploadFile(image) == true) { 
          
          imagesaved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(imagesaved)) { 
            commuthumb = Tool.preview(upDir, imagesaved, 200, 150);
          }

          commuVO.setImage(image); 
          commuVO.setImagesaved(imagesaved); 
          commuVO.setCommuthumb(commuthumb);
          commuVO.setImagesize(imagesize); 

        } else {
          ra.addFlashAttribute("code", "check_upload_file_fail"); 
          ra.addFlashAttribute("cnt", 0); 
          ra.addFlashAttribute("url", "/commu/msg"); 
          return "redirect:/commu/msg"; 
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int memberno = (int) session.getAttribute("memberno"); 
      commuVO.setMemberno(memberno);
      int cnt = this.commuProc.create(commuVO);

      if (cnt == 1) {

        ra.addAttribute("clubno", commuVO.getClubno()); 
        return "redirect:/commu/list_by_clubno";

      } else {
        ra.addFlashAttribute("code", "create_fail"); 
        ra.addFlashAttribute("cnt", 0); 
        ra.addFlashAttribute("url", "/commu/msg"); 
        return "redirect:/commu/msg";
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/member/login_cookie_need"; 
    }
  }
    
 
 /**
  * 전체 목록
  */
 @GetMapping(value = "/list_all")
 public String list_all(HttpSession session, Model model,
                         @RequestParam(name = "clubno", defaultValue = "1") int clubno) {

     // 메뉴 정보 가져오기
     ArrayList<ClubVOMenu> menu = this.clubProc.menu();
     model.addAttribute("menu", menu);

     // 전체 목록 가져오기
     ArrayList<CommuVO> list = this.commuProc.list_all();
     model.addAttribute("list", list);

     model.addAttribute("clubno", clubno);

     // 클럽 정보 가져오기 (clubno에 해당하는 클럽 정보)
     ClubVO clubVO = this.clubProc.read(clubno);
     model.addAttribute("clubVO", clubVO);

     return "/commu/list_all";
 }

 /**
  * 클럽별 목록 + 검색 + 페이징
  * @return
  */
 @GetMapping(value = "/list_by_clubno")
 public String list_by_clubno_search_paging(HttpSession session, Model model, 
     @RequestParam(name = "clubno", defaultValue = "1") int clubno,
     @RequestParam(name = "hashtag", defaultValue = "") String hashtag,
     @RequestParam(name = "now_page", defaultValue = "1") int now_page) {


   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);

   ClubVO clubVO = this.clubProc.read(clubno);
   model.addAttribute("clubVO", clubVO);

   hashtag = Tool.checkNull(hashtag).trim();

   HashMap<String, Object> map = new HashMap<>();
   map.put("clubno", clubno);
   map.put("hashtag", hashtag);
   map.put("now_page", now_page);

   ArrayList<CommuVO> list = this.commuProc.list_by_clubno_search_paging(map);
   model.addAttribute("list", list);

   model.addAttribute("hashtag", hashtag);

   int search_count = this.commuProc.list_by_clubno_search_count(map);
   String paging = this.commuProc.pagingBox(clubno, now_page, hashtag, "/commu/list_by_clubno", search_count,
       Commu.RECORD_PER_PAGE, Commu.PAGE_PER_BLOCK);
   model.addAttribute("paging", paging);
   model.addAttribute("now_page", now_page);

   model.addAttribute("search_count", search_count);

   // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
   int no = search_count - ((now_page - 1) * Commu.RECORD_PER_PAGE);
   model.addAttribute("no", no);

   return "/commu/list_by_clubno_search_paging";
 }  
 
 /**
  * 클럽별 목록 + 검색 + 페이징 + Grid
  * @return
  */
 @GetMapping(value = "/list_by_clubno_grid")
 public String list_by_clubno_search_paging_grid(HttpSession session, Model model, 
     @RequestParam(name = "clubno", defaultValue = "0") int clubno,
     @RequestParam(name = "hashtag", defaultValue = "") String hashtag,
     @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);

   ClubVO clubVO = this.clubProc.read(clubno);
   model.addAttribute("clubVO", clubVO);

   hashtag = Tool.checkNull(hashtag).trim();

   HashMap<String, Object> map = new HashMap<>();
   map.put("clubno", clubno);
   map.put("hashtag", hashtag);
   map.put("now_page", now_page);

   ArrayList<CommuVO> list = this.commuProc.list_by_clubno_search_paging(map);
   model.addAttribute("list", list);

   model.addAttribute("hashtag", hashtag);

   int search_count = this.commuProc.list_by_clubno_search_count(map);
   String paging = this.commuProc.pagingBox(clubno, now_page, hashtag, "/commu/list_by_clubno_grid", search_count,
       Commu.RECORD_PER_PAGE, Commu.PAGE_PER_BLOCK);
   model.addAttribute("paging", paging);
   model.addAttribute("now_page", now_page);

   model.addAttribute("search_count", search_count);

   int no = search_count - ((now_page - 1) * Commu.RECORD_PER_PAGE);
   model.addAttribute("no", no);


   return "/commu/list_by_clubno_search_paging_grid";
 }

 /**
  * 조회 
  */
 @GetMapping(value = "/read")
 public String read(Model model, 
     @RequestParam(name="communo", defaultValue = "0") int communo, 
     @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
     @RequestParam(name="now_page", defaultValue = "1") int now_page,
     @RequestParam(name="action", defaultValue = "") String action) {
   
   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);

   CommuVO commuVO = this.commuProc.read(communo);

   long imagesize = commuVO.getImagesize();
   String size1_label = Tool.unit(imagesize);
   commuVO.setSize1_label(size1_label);

   model.addAttribute("commuVO", commuVO);

   ClubVO clubVO = this.clubProc.read(commuVO.getClubno());
   model.addAttribute("clubVO", clubVO);

   model.addAttribute("hashtag", hashtag);
   model.addAttribute("now_page", now_page);

   return "/commu/read";
 }
 
 /**
  * Youtube 등록/수정/삭제 폼 
  */
 @GetMapping(value = "/youtube")
 public String youtube(Model model, 
     @RequestParam(name="communo", defaultValue="0") int communo,
     @RequestParam(name="hashtag", defaultValue="")  String hashtag, 
     @RequestParam(name="now_page", defaultValue="0") int now_page) {
   
   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);

   CommuVO commuVO = this.commuProc.read(communo); 
   model.addAttribute("commuVO", commuVO); 
   
   ClubVO clubVO = this.clubProc.read(commuVO.getClubno()); 
   model.addAttribute("clubVO", clubVO);

   model.addAttribute("hashtag", hashtag);
   model.addAttribute("now_page", now_page);
   
   return "/commu/youtube";  
 }

 /**
  * Youtube 등록/수정/삭제 처리
  */
 @PostMapping(value = "/youtube")
 public String youtube_update(Model model, 
                                           RedirectAttributes ra,
                                           @RequestParam(name="communo", defaultValue = "0") int communo, 
                                           @RequestParam(name="youtube", defaultValue = "") String youtube, 
                                           @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
                                           @RequestParam(name="now_page", defaultValue = "0") int now_page) {

   if (youtube.trim().length() > 0) { 
     youtube = Tool.youtubeResize(youtube, 640); 
   }

   HashMap<String, Object> hashMap = new HashMap<String, Object>();
   hashMap.put("communo", communo);
   hashMap.put("youtube", youtube);

   this.commuProc.youtube(hashMap);
   
   ra.addAttribute("communo", communo);
   ra.addAttribute("hashtag", hashtag);
   ra.addAttribute("now_page", now_page);

   return "redirect:/commu/read";
 }
 

 /**
  * 파일 수정 폼
  *
  */
 @GetMapping(value = "/update_text")
 public String update_text(HttpSession session, Model model, RedirectAttributes ra,
     @RequestParam(name="communo", defaultValue = "0") int communo,       
     @RequestParam(name="hashtag", defaultValue = "") String hashtag,
     @RequestParam(name="now_page", defaultValue = "0") int now_page) {
   
   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);

   model.addAttribute("hashtag", hashtag);
   model.addAttribute("now_page", now_page);

   if (this.memberProc.isMember(session)) { 
     CommuVO commuVO = this.commuProc.read(communo);
     model.addAttribute("commuVO", commuVO);

     ClubVO clubVO = this.clubProc.read(commuVO.getClubno());
     model.addAttribute("clubVO", clubVO);

     return "/commu/update_text"; 


   } else {
 
     return "/member/login_cookie_need";
   }

 }

 /**
  * 파일 수정 처리 
  * 
  * @return
  */
 @PostMapping(value = "/update_text")
 public String update_text(HttpSession session, Model model, RedirectAttributes ra,
     @ModelAttribute("commuVO") CommuVO commuVO,
     @RequestParam(name = "search_hashtag", defaultValue = "") String search_hashtag,
     @RequestParam(name = "now_page", defaultValue = "0") int now_page) {

   ra.addAttribute("hashtag", search_hashtag);
   ra.addAttribute("now_page", now_page);

   if (this.memberProc.isMember(session)) { // 관리자 로그인 확인
     this.commuProc.update_text(commuVO); // 글수정

     ra.addAttribute("communo", commuVO.getCommuno());
     ra.addAttribute("clubno", commuVO.getClubno());
     return "redirect:/commu/read";

   } else { // 정상적인 로그인이 아닌 경우 로그인 유도
     ra.addAttribute("url", "/member/login_cookie_need");
     return "redirect:/commu/post2get";
   }

 }
 
 /**
  * 파일 수정 폼 
  * 
  * @return
  */
 @GetMapping(value = "/update_file")
 public String update_file(HttpSession session, Model model, 
                                    @RequestParam(name="communo", defaultValue = "0") int communo,
                                    @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
   
   ArrayList<ClubVOMenu> menu = this.clubProc.menu();
   model.addAttribute("menu", menu);
   
   model.addAttribute("hashtag", hashtag);
   model.addAttribute("now_page", now_page);
   
   CommuVO commuVO = this.commuProc.read(communo);
   model.addAttribute("commuVO", commuVO);

   ClubVO clubVO = this.clubProc.read(commuVO.getClubno());
   model.addAttribute("clubVO", clubVO);

   return "/commu/update_file";

 }

 /**
  * 파일 수정 처리
  * @return
  */
 @PostMapping(value = "/update_file")
 public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                                    @ModelAttribute("commuVO") CommuVO commuVO,
                                    @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
                                    @RequestParam(name="now_page", defaultValue = "1") int now_page) {
   if (this.memberProc.isMember(session)) {

     CommuVO commuVO_old = commuProc.read(commuVO.getCommuno());

     // -------------------------------------------------------------------
     // 파일 삭제 시작
     // -------------------------------------------------------------------
     String imagesaved = commuVO_old.getImagesaved(); // 실제 저장된 파일명
     String commuthumb = commuVO_old.getCommuthumb(); // 실제 저장된 preview 이미지 파일명
     long imagesize = 0;

     String upDir = Commu.getUploadDir(); 

     Tool.deleteFile(upDir, imagesaved); // 실제 저장된 파일삭제
     Tool.deleteFile(upDir, commuthumb); // preview 이미지 삭제
     // -------------------------------------------------------------------
     // 파일 삭제 종료
     // -------------------------------------------------------------------

     // -------------------------------------------------------------------
     // 파일 전송 시작
     // -------------------------------------------------------------------
     String image = ""; // 원본 파일명 image

     // 전송 파일이 없어도 file1MF 객체가 생성됨.
     // <input type='file' class="form-control" name='file1MF' id='file1MF'
     // value='' placeholder="파일 선택">
     MultipartFile mf = commuVO.getFile1MF();

     image = mf.getOriginalFilename(); // 원본 파일명
     imagesize = mf.getSize(); // 파일 크기

     if (imagesize > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
       // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
       imagesaved = Upload.saveFileSpring(mf, upDir);

       if (Tool.isImage(imagesaved)) { // 이미지인지 검사
         // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
         commuthumb = Tool.preview(upDir, imagesaved, 250, 200);
       }

     } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
       image = "";
       imagesaved = "";
       commuthumb = "";
       imagesize = 0;
     }

     commuVO.setImage(image);
     commuVO.setImagesaved(imagesaved);
     commuVO.setCommuthumb(commuthumb);
     commuVO.setImagesize(imagesize);
     // -------------------------------------------------------------------
     // 파일 전송 코드 종료
     // -------------------------------------------------------------------

     this.commuProc.update_file(commuVO); // Oracle 처리
     ra.addAttribute ("communo", commuVO.getCommuno());
     ra.addAttribute("clubno", commuVO.getClubno());
     ra.addAttribute("hashtag", hashtag);
     ra.addAttribute("now_page", now_page);
     
     return "redirect:/commu/read";
   } else {
     ra.addAttribute("url", "/member/login_cookie_need"); 
     return "redirect:/commu/msg"; // GET
   }
 }


 /**
  * 파일 삭제 폼
  * @return
  */
 @GetMapping(value = "/delete")
 public String delete(HttpSession session, Model model, RedirectAttributes ra,
     @RequestParam(name="clubno", defaultValue = "0") int clubno,
     @RequestParam(name="communo", defaultValue = "0") int communo,
     @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
   
   if (this.memberProc.isMember(session)) { 
     model.addAttribute("clubno", clubno);
     model.addAttribute("hashtag", hashtag);
     model.addAttribute("now_page", now_page);
     
     ArrayList<ClubVOMenu> menu = this.clubProc.menu();
     model.addAttribute("menu", menu);
     
     CommuVO commuVO = this.commuProc.read(communo);
     model.addAttribute("commuVO", commuVO);
     
     ClubVO clubVO = this.clubProc.read(commuVO.getClubno());
     model.addAttribute("clubVO", clubVO);
     
     return "/commu/delete"; 
     
   } else {
     ra.addAttribute("url", "/member/login_cookie_need");
     return "redirect:/commu/msg"; 
   }

 }
 
 
 /**
  * 파일 삭제 처리
  * 
  * @return
  */
 @PostMapping(value = "/delete")
 public String delete(RedirectAttributes ra,
     @RequestParam(name="clubno", defaultValue = "0") int clubno,
     @RequestParam(name="communo", defaultValue = "0") int communo,
     @RequestParam(name="hashtag", defaultValue = "") String hashtag, 
     @RequestParam(name="now_page", defaultValue = "1") int now_page) {
   
   // -------------------------------------------------------------------
   // 파일 삭제 시작
   // -------------------------------------------------------------------
   // 삭제할 파일 정보를 읽어옴.
   CommuVO commuVO_read = commuProc.read(communo);
       
   String imagesaved = commuVO_read.getImagesaved();
   String commuthumb = commuVO_read.getCommuthumb();
   
   String uploadDir = Commu.getUploadDir();
   Tool.deleteFile(uploadDir, imagesaved); 
   Tool.deleteFile(uploadDir, commuthumb);   
   // -------------------------------------------------------------------
   // 파일 삭제 종료
   // -------------------------------------------------------------------
       
   this.commuProc.delete(communo);
       
   // -------------------------------------------------------------------------------------
   // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
   // -------------------------------------------------------------------------------------    
   // 마지막 페이지의 마지막 10번째 레코드를 삭제후
   // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
   // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
   
   HashMap<String, Object> map = new HashMap<String, Object>();
   map.put("clubno", clubno);
   map.put("hashtag", hashtag);
   
   if (this.commuProc.list_by_clubno_search_count(map) % Commu.RECORD_PER_PAGE == 0) {
     now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
     if (now_page < 1) {
       now_page = 1; // 시작 페이지
     }
   }
   // -------------------------------------------------------------------------------------

   ra.addAttribute("clubno", clubno);
   ra.addAttribute("hashtag", hashtag);
   ra.addAttribute("now_page", now_page);
   
   return "redirect:/commu/list_by_clubno";    
   
 }   
    

 
}