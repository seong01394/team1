package dev.mvc.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.LoginLog.LoginLogProcInter;
import dev.mvc.LoginLog.LoginLogVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/member")
@Controller
public class MemberCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")  // @Service("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  @Autowired
  @Qualifier("dev.mvc.LoginLog.LoginLogProc")
  private LoginLogProcInter loginlogProc;
  
//  @Autowired
//  @Qualifier("dev.mvc.part.PartProc")
//  private PartProcInter partProc;
  
  public MemberCont() {
    System.out.println("-> MemberCont created.");  
  }
  
  @GetMapping(value="/checkID") // http://localhost:9091/member/checkID?id=admin
  @ResponseBody
  public String checkID(@RequestParam(name="id", defaultValue = "") String id) {    
    System.out.println("-> id: " + id);
    int cnt = this.memberProc.checkID(id);
    
    // return "cnt: " + cnt;
    // return "{\"cnt\": " + cnt + "}";    // {"cnt": 1} JSON
    
    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);
    
    return obj.toString();
  }
  
  /**
   * 닉네임 중복 확인
   * 
   * @param nickname
   * @return
   */
  @GetMapping(value = "/checkNICKNAME") // http://localhost:9093/member/checkNICKNAME?nickname=test
  @ResponseBody
  public String checkNICKNAME(@RequestParam(name = "nickname", defaultValue = "") String nickname) {
    System.out.println("-> nickname: " + nickname);
    int cnt = this.memberProc.checkNICKNAME(nickname);
    

    JSONObject obj = new JSONObject();
    obj.put("cnt", cnt);

    return obj.toString();
  }
  
  /**
   * 회원 가입 폼
   * @param model
   * @param memberVO
   * @return
   */
  @GetMapping(value="/create") // http://localhost:9091/member/create
  public String create_form(Model model, 
                                      @ModelAttribute("memberVO") MemberVO memberVO) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    return "/th/member/create";    // /template/member/create.html
  }
  
  @PostMapping(value="/create")
  public String create_proc(Model model,
                                     @ModelAttribute("memberVO") MemberVO memberVO) {
    int checkID_cnt = this.memberProc.checkID(memberVO.getId());
    
    if (checkID_cnt == 0) {
      memberVO.setGrade(15); // 기본 회원 15
      
      // memberVO.setPasswd(new Security().aesEncode(memberVO.getPasswd())); // 단축형
      
      int cnt = this.memberProc.create(memberVO);
      
      if (cnt == 1) {
        model.addAttribute("code", "create_success");
        model.addAttribute("name", memberVO.getName());
        model.addAttribute("id", memberVO.getId());
      } else {
        model.addAttribute("code", "create_fail");
      }
      
      model.addAttribute("cnt", cnt);
    } else { // id 중복
      model.addAttribute("code", "duplicte_fail");
      model.addAttribute("cnt", 0);
    }
    
    return "/th/member/msg"; // /templates/member/msg.html
  }
  
  @GetMapping(value="/list")
  public String list(HttpSession session, Model model) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMemberAdmin(session)) {
      ArrayList<MemberVO> list = this.memberProc.list();
      
      model.addAttribute("list", list);
      
      return "/th/member/list";  // /templates/member/list.html
    } else {
      return "redirect:/th/member/login_cookie_need.html";  // redirect
    }
  }
  
//  /**
//   * 조회
//   * @param model
//   * @param memberno 회원 번호
//   * @return 회원 정보
//   */
//  @GetMapping(value="/read")
//  public String read(Model model,
//                            @RequestParam(name="memberno", defaultValue = "") int memberno) {
//    System.out.println("-> read memberno: " + memberno);
//    
//    MemberVO memberVO = this.memberProc.read(memberno);
//    model.addAttribute("memberVO", memberVO);
//    
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
//    
//    return "/member/read";  // templates/member/read.html
//  }

  /**
   * 조회
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/read")
  public String read(HttpSession session, 
                            Model model,
                            @RequestParam(name="memberno", defaultValue = "") int memberno) {
    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 사용자: 11 ~ 20
    // int gradeno = this.memberProc.read(memberno).getGrade(); // 등급 번호
    String grade = (String)session.getAttribute("grade"); // 등급: admin, member, guest
    
    // 사용자: member && 11 ~ 20
    // if (grade.equals("member") && (gradeno >= 11 && gradeno <= 20) && memberno == (int)session.getAttribute("memberno")) {
    if (grade.equals("member") &&  memberno == (int)session.getAttribute("memberno")) {
      System.out.println("-> read memberno: " + memberno);
      
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "/th/member/read";  // templates/member/read.html
      
    } else if (grade.equals("admin")) {
      System.out.println("-> read memberno: " + memberno);
      
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);
      
      return "/th/member/read";  // templates/member/read.html
    } else {
      return "redirect:/th/member/login_cookie_need.html";  // redirect
    }
    
  }
  
  /**
   * 회원 정보 수정 폼(아이디, 비밀번호 제외) 회원은 자기 계정만, 관리자도 자기 계정만 수정 가능(회원 계정 수정X)
   * 
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/update")
  public String update_form(HttpSession session, Model model,
      @RequestParam(name = "nickname", required = false) String nickname,
      @RequestParam(name = "memberno", required = false) Integer memberno) {

    // 회원은 회원 등급만 처리, 관리자: 1 ~ 10, 회원: 11 ~ 20
    memberno = (int) session.getAttribute("memberno");

    // 회원: member && 11 ~ 20
    if (this.memberProc.isMember(session) && memberno == (int) session.getAttribute("memberno")) {
      System.out.println("memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/update"; // templates/th/member/read.html

    } else if (this.memberProc.isMemberAdmin(session) && memberno == (int) session.getAttribute("memberno")) { // 관리자 1~10
      System.out.println("-> admin memberno: " + memberno);
      MemberVO memberVO = this.memberProc.read(memberno);

      model.addAttribute("memberVO", memberVO);

      return "/th/member/update"; // templates/member/update.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }

  }

  /**
   * 회원 정보 수정 처리(아이디, 비밀번호 제외) 회원은 자기 계정만, 관리자도 자기 계정만 수정 가능(회원 계정 수정X)
   * 
   * @param session
   * @param model
   * @param memberno
   * @param memberVO
   * @return
   */
  @PostMapping(value = "/update")
  public String update_proc(HttpSession session, Model model,
       @ModelAttribute("memberVO") MemberVO memberVO) {
    
    String nicknameSession = (String) session.getAttribute("nickname");
    
    // 회원 본인일 때
    if (this.memberProc.isMember(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int checkNICKNAME_cnt = this.memberProc.checkNICKNAME(memberVO.getNickname());
      // 닉네임 중복되지 않았을 경우
      if (checkNICKNAME_cnt == 0) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress1(memberVO.getAddress1().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt1: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress1());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      // 닉네임 중복됐지만 기존 회원 닉네임이랑 같을 경우
      else if (checkNICKNAME_cnt == 1 && nicknameSession.equals(memberVO.getNickname())) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress1(memberVO.getAddress1().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress1());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
          return "/th/member/msg";
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      return "/th/member/msg"; // templates/th/member/read.html
    } // 관리자 본인일 때
    else if (this.memberProc.isMemberAdmin(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int checkNICKNAME_cnt = this.memberProc.checkNICKNAME(memberVO.getNickname());
      // 닉네임 중복되지 않았을 경우
      if (checkNICKNAME_cnt == 0) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress1(memberVO.getAddress1().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress1());
        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      // 닉네임 중복됐지만 기존 회원 닉네임이랑 같을 경우
      else if (checkNICKNAME_cnt == 1 && nicknameSession.equals(memberVO.getNickname())) {
        memberVO.setMemberno(memberVO.getMemberno());
        memberVO.setName(memberVO.getName().trim());
        memberVO.setNickname(memberVO.getNickname().trim());
        memberVO.setTel(memberVO.getTel().trim());
        memberVO.setZipcode(memberVO.getZipcode().trim());
        memberVO.setAddress1(memberVO.getAddress1().trim());

        int cnt = this.memberProc.update(memberVO);
        System.out.println("update cnt: " + cnt);

        if (cnt == 1) {
          model.addAttribute("code", "update_success");
          model.addAttribute("memberno", memberVO.getMemberno());
          model.addAttribute("name", memberVO.getName());
          model.addAttribute("nickname", memberVO.getNickname());
          model.addAttribute("tel", memberVO.getTel());
          model.addAttribute("zipcode", memberVO.getZipcode());
          model.addAttribute("address", memberVO.getAddress1());

        } else {
          model.addAttribute("code", "update_fail");
          System.out.println("update_fail");
        }

        model.addAttribute("cnt", cnt);
        System.out.println("update_success_cnt: " + cnt);
      }
      return "redirect:/member/msg"; // /templates/member/read.html
    } else {
      return "redirect:/member/login_cookie_need"; // /templates/member/read.html
    }
  }
  
  /**
   * 회원 탈퇴 폼
   * @param session
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value = "/bye")
  public String withdraw_form(HttpSession session, Model model,
      @RequestParam(name = "memberno", required = false) Integer memberno) {


    if (this.memberProc.isMember(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/bye";
    } else if (this.memberProc.isMemberAdmin(session)) {
      memberno = (int) session.getAttribute("memberno");
      MemberVO memberVO = this.memberProc.read(memberno);
      model.addAttribute("memberVO", memberVO);

      return "/th/member/bye";   // /templates/th/member/withdraw.html
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }

  /**
   * 회원 탈퇴 처리
   * 
   * @param model
   * @param memberno 삭제할 회원 번호
   * @return
   */
  @PostMapping(value = "/bye")
  public String withdraw_process(HttpSession session, Model model, @ModelAttribute("memberVO") MemberVO memberVO) {
    //회원이면
    if (this.memberProc.isMember(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int cnt = this.memberProc.bye(memberVO);

      if (cnt == 1) {
        model.addAttribute("code", "bye_success");
        model.addAttribute("memberno", memberVO.getMemberno());
        model.addAttribute("grade", memberVO.getGrade());
        
        System.out.println("memberno: " + memberVO.getMemberno() + ", id: " + memberVO.getId() + "nickname: " + memberVO.getNickname() + " has withdrown");
        
        session.invalidate();
        return "/th/member/msg";
      } else {
        model.addAttribute("code", "bye_fail");
        return "/th/member/msg";      // /templates/th/member/error.html
      }
    }
    // 관리자이면
    else if (this.memberProc.isMemberAdmin(session) && memberVO.getMemberno() == (int) session.getAttribute("memberno")) {
      int cnt = this.memberProc.bye(memberVO);

      if (cnt == 1) {
        model.addAttribute("code", "bye_success");
        model.addAttribute("memberno", memberVO.getMemberno());
        model.addAttribute("grade", memberVO.getGrade());
        
        System.out.println("memberno: " + memberVO.getMemberno() + "id: " + memberVO.getId() + "nickname: " + memberVO.getNickname() + " has withdrown");
        
        session.invalidate();
        return "/th/member/msg";
      } else {
        model.addAttribute("code", "bye_fail");
        return "/th/member/msg";      // /templates/th/member/error.html
      }
    } else {
      return "redirect:/member/login_cookie_need"; // redirect
    }
  }
  
//  /**
//   * 로그인
//   * @param model
//   * @param memberno 회원 번호
//   * @return 회원 정보
//   */
//  @GetMapping(value="/login")
//  public String login_form(Model model) {
//    return "/member/login";   // templates/member/login.html
//  }
  
//  /**
//   * 로그인 처리
//   * @param model
//   * @param memberno 회원 번호
//   * @return 회원 정보
//   */
//  @PostMapping(value="/login")
//  public String login_proc(HttpSession session, Model model, 
//                                    @RequestParam(name="id", defaultValue = "") String id,
//                                    @RequestParam(name="passwd", defaultValue = "") String passwd) {
//    HashMap<String, Object> map = new HashMap<String, Object>();
//    map.put("id", id);
//    map.put("passwd", passwd);
//    
//    int cnt = this.memberProc.login(map);
//    System.out.println("-> login_proc cnt: " + cnt);
//    
//    model.addAttribute("cnt", cnt);
//    
//    if (cnt == 1) {
//      MemberVO memverVO = this.memberProc.readById(id);
//      session.setAttribute("memberno", memverVO.getMemberno());
//      session.setAttribute("id", memverVO.getId());
//      session.setAttribute("mname", memverVO.getMname());
//      session.setAttribute("grade", memverVO.getGrade());
//      
//      return "redirect:/";
//    } else {
//      model.addAttribute("code", "login_fail");
//      return "/member/msg";
//    }
//    
//  }
  


  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 시작
  // ----------------------------------------------------------------------------------
  /**
   * 로그인
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login")
  public String login_form(Model model, HttpServletRequest request) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){                     // 아이디
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){        // 아이디 저장 여부
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){       // 패스워드
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){ // 패스워드 저장 여부
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    
    //    <input type='text' class="form-control" name='id' id='id' 
    //            th:value='${ck_id }' required="required" 
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);
  
    //    <input type='checkbox' name='id_save' value='Y' 
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");
    
    return "/th/member/login";  // /templates/member/login_cookie.html
  }

  /**
   * Cookie 기반 로그인 처리
   * @param session
   * @param request
   * @param response
   * @param model
   * @param id 아이디
   * @param passwd 패스워드
   * @param id_save 아이디 저장 여부
   * @param passwd_save 패스워드 저장 여부
   * @return
   */
  @PostMapping(value="/login")
  public String login_proc(HttpSession session,
                                     HttpServletRequest request,
                                     HttpServletResponse response,
                                     Model model, 
                                     RedirectAttributes ra,
                                     @RequestParam(value="id", defaultValue = "") String id, 
                                     @RequestParam(value="passwd", defaultValue = "") String passwd,
                                     @RequestParam(value="id_save", defaultValue = "") String id_save,
                                     @RequestParam(value="passwd_save", defaultValue = "") String passwd_save
                                     ) {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("id", id);
    map.put("passwd", passwd);
    
    int cnt = this.memberProc.login(map);
    System.out.println("-> login_proc cnt: " + cnt);
    
    String ip = request.getRemoteAddr();
    
    LoginLogVO loginlogVO = new LoginLogVO();
    loginlogVO.setId(id);
    loginlogVO.setIp(ip);
    loginlogVO.setResult(cnt == 1 ? "T" : "F");
    
    this.loginlogProc.login_log(loginlogVO);
    
    
    if (cnt == 1) {// 로그인 성공
      // id를 이용하여 회원 정보 조회
      MemberVO memberVO = this.memberProc.readById(id);
      if(memberVO.getGrade() == 99) {
        ra.addFlashAttribute("cnt", 0);
        ra.addFlashAttribute("code", "delete member");
        return "redirect:/member/login";
      }     
      session.setAttribute("memberno", memberVO.getMemberno());
      session.setAttribute("id", memberVO.getId());
      session.setAttribute("name", memberVO.getName());
      session.setAttribute("nickname", memberVO.getNickname());
      session.setAttribute("user_level", memberVO.getUser_level());
      session.setAttribute("grade", memberVO.getGrade());
 
      // session.setAttribute("grade", memverVO.getGrade());
      
      if (memberVO.getGrade() >= 1 && memberVO.getGrade() <= 10) {
        session.setAttribute("grade", "admin");
      } else if (memberVO.getGrade() >= 11 && memberVO.getGrade() <= 20) {
        session.setAttribute("grade", "member");
      } else if (memberVO.getGrade() >= 41&& memberVO.getGrade() <= 99) {
        session.setAttribute("grade", "stooped");
      }
      System.out.println("grade:" + memberVO.getGrade());
      System.out.println("출력:" + memberVO.getNickname());
      System.out.println("출력:" + memberVO.getGrade());
     
      
      
      
      // Cookie 관련 코드---------------------------------------------------------
      // -------------------------------------------------------------------
      // id 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (id_save.equals("Y")) { // id를 저장할 경우, Checkbox를 체크한 경우
        Cookie ck_id = new Cookie("ck_id", id);
        ck_id.setPath("/");  // root 폴더에 쿠키를 기록함으로 모든 경로에서 쿠기 접근 가능
        ck_id.setMaxAge(60 * 60 * 24 * 30); // 30 day, 초단위
        response.addCookie(ck_id); // id 저장
      } else { // N, id를 저장하지 않는 경우, Checkbox를 체크 해제한 경우
        Cookie ck_id = new Cookie("ck_id", "");
        ck_id.setPath("/");
        ck_id.setMaxAge(0); // 0초
        response.addCookie(ck_id); // id 저장
      }
      
      // id를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_id_save = new Cookie("ck_id_save", id_save);
      ck_id_save.setPath("/");
      ck_id_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_id_save);
      // -------------------------------------------------------------------
  
      // -------------------------------------------------------------------
      // Password 관련 쿠기 저장
      // -------------------------------------------------------------------
      if (passwd_save.equals("Y")) { // 패스워드 저장할 경우
        Cookie ck_passwd = new Cookie("ck_passwd", passwd);
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(60 * 60 * 24 * 30); // 30 day
        response.addCookie(ck_passwd);
      } else { // N, 패스워드를 저장하지 않을 경우
        Cookie ck_passwd = new Cookie("ck_passwd", "");
        ck_passwd.setPath("/");
        ck_passwd.setMaxAge(0);
        response.addCookie(ck_passwd);
      }
      // passwd를 저장할지 선택하는  CheckBox 체크 여부
      Cookie ck_passwd_save = new Cookie("ck_passwd_save", passwd_save);
      ck_passwd_save.setPath("/");
      ck_passwd_save.setMaxAge(60 * 60 * 24 * 30); // 30 day
      response.addCookie(ck_passwd_save);
      // -------------------------------------------------------------------
      // ----------------------------------------------------------------------------     
      
      return "/th/index";
    } else {
      ra.addFlashAttribute("cnt", cnt);
      model.addAttribute("code", "login_fail");
      return "/member/login";
    }
  }
  
  // ----------------------------------------------------------------------------------
  // Cookie 사용 로그인 관련 코드 종료
  // ----------------------------------------------------------------------------------
  
  /**
   * 관리자 로그인
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/admin_login")
  public String admin_login_form(Model model, HttpServletRequest request) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = ""; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = ""; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){                     // 아이디
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){        // 아이디 저장 여부
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){       // 패스워드
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){ // 패스워드 저장 여부
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    
    //    <input type='text' class="form-control" name='id' id='id' 
    //            th:value='${ck_id }' required="required" 
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);
  
    //    <input type='checkbox' name='id_save' value='Y' 
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");
    
    return "/th/member/admin_login";  // /templates/member/login_cookie.html
  }
  /**
   * 로그아웃
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/logout")
  public String logout(HttpSession session, Model model) {
    session.invalidate();  // 모든 세션 변수 삭제
    return "redirect:/";
  }
  
  /**
   * 패스워드 수정 폼
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value="/passwd_update")
  public String passwd_update_form(HttpSession session, Model model) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
      
      MemberVO memberVO = this.memberProc.read(memberno);
      
      model.addAttribute("memberVO", memberVO);
      
      return "/th/member/passwd_update";    // /templates/member/passwd_update.html      
    } else {
      return "redirect:/th/member/login_cookie_need.html"; // redirect
    }

  }
  
  /**
   * 현재 패스워드 확인
   * @param session
   * @param current_passwd
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value="/passwd_check")
  @ResponseBody
  public String passwd_check(HttpSession session, @RequestBody String json_src) {
    System.out.println("-> json_src: " + json_src); // json_src: {"current_passwd":"1234"}
    
    JSONObject src = new JSONObject(json_src); // String -> JSON
    
    String current_passwd = (String)src.get("current_passwd"); // 값 가져오기
    System.out.println("-> current_passwd: " + current_passwd);
    
    try {
      Thread.sleep(3000);
    } catch(Exception e) {
      
    }
    
    int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("memberno", memberno);
    map.put("passwd", current_passwd);
    
    int cnt = this.memberProc.passwd_check(map); // 현재 로그인한 사용자의 패스워드 확인
    
    JSONObject json = new JSONObject();
    json.put("cnt", cnt);  // 1: 패스워드 일치, 0: 불일치
    System.out.println(json.toString());
    
    return json.toString();   
  }
  
  /**
   * 패스워드 변경
   * @param session
   * @param model
   * @param current_passwd 현재 패스워드
   * @param passwd 새로운 패스워드
   * @return
   */
  @PostMapping(value="/passwd_update_proc")
  public String passwd_update_proc(HttpSession session, 
                                                    Model model, 
                                                    @RequestParam(value="current_passwd", defaultValue = "") String current_passwd, 
                                                    @RequestParam(value="passwd", defaultValue = "") String passwd) {
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("memberno", memberno);
      map.put("passwd", current_passwd);
   
      int cnt = this.memberProc.passwd_check(map);
      
      if (cnt == 0) { // 패스워드 불일치
        model.addAttribute("code", "passwd_not_equal");
        model.addAttribute("cnt", 0);
        
      } else { // 패스워드 일치
        map = new HashMap<String, Object>();
        map.put("memberno", memberno);
        map.put("passwd", passwd); // 새로운 패스워드

        int passwd_change_cnt = this.memberProc.passwd_update(map);
        
        if (passwd_change_cnt == 1) {
          model.addAttribute("code", "passwd_change_success");
          model.addAttribute("cnt", 1);
        } else {
          model.addAttribute("code", "passwd_change_fail");
          model.addAttribute("cnt", 0);
        }
      }

      return "/th/member/msg";   // /templates/member/msg.html
    } else {
      return "redirect:/th/member/login_cookie_need.html"; // redirect
    }

  }

  /**
   * 로그인 요구에 따른 로그인 폼 출력 
   * @param model
   * @param memberno 회원 번호
   * @return 회원 정보
   */
  @GetMapping(value="/login_cookie_need")
  public String login_cookie_need(Model model, HttpServletRequest request) {
    // Cookie 관련 코드---------------------------------------------------------
    Cookie[] cookies = request.getCookies();
    Cookie cookie = null;
  
    String ck_id = "user4"; // id 저장
    String ck_id_save = ""; // id 저장 여부를 체크
    String ck_passwd = "3139"; // passwd 저장
    String ck_passwd_save = ""; // passwd 저장 여부를 체크
  
    if (cookies != null) { // 쿠키가 존재한다면
      for (int i=0; i < cookies.length; i++){
        cookie = cookies[i]; // 쿠키 객체 추출
      
        if (cookie.getName().equals("ck_id")){
          ck_id = cookie.getValue();  // email
        }else if(cookie.getName().equals("ck_id_save")){
          ck_id_save = cookie.getValue();  // Y, N
        }else if (cookie.getName().equals("ck_passwd")){
          ck_passwd = cookie.getValue();         // 1234
        }else if(cookie.getName().equals("ck_passwd_save")){
          ck_passwd_save = cookie.getValue();  // Y, N
        }
      }
    }
    // ----------------------------------------------------------------------------
    
    //    <input type='text' class="form-control" name='id' id='id' 
    //            th:value='${ck_id }' required="required" 
    //            style='width: 30%;' placeholder="아이디" autofocus="autofocus">
    model.addAttribute("ck_id", ck_id);
  
    //    <input type='checkbox' name='id_save' value='Y' 
    //            th:checked="${ck_id_save == 'Y'}"> 저장
    model.addAttribute("ck_id_save", ck_id_save);
  
    model.addAttribute("ck_passwd", ck_passwd);
    model.addAttribute("ck_passwd_save", ck_passwd_save);

//    model.addAttribute("ck_id_save", "Y");
//    model.addAttribute("ck_passwd_save", "Y");
    
    return "/th/member/login_cookie_need";  // templates/member/login_cookie_need.html
  }
  
  /**
   * 패스워드 수정 폼
   * @param model
   * @param memberno
   * @return
   */
  @GetMapping(value="/id_update")
  public String id_update_form(HttpSession session, Model model) {
//    ArrayList<PartVOMenu> menu = this.partProc.menu();
//    model.addAttribute("menu", menu);
    
    if (this.memberProc.isMember(session)) {
      int memberno = (int)session.getAttribute("memberno"); // session에서 가져오기
 
      MemberVO memberVO = this.memberProc.read(memberno);

      model.addAttribute("memberVO", memberVO);

      
      return "/th/member/id_update";    // /templates/member/passwd_update.html      
    } else {
      return "redirect:/th/member/login_cookie_need.html"; // redirect
    }

  }
  
  /**
   * 현재 아이디 확인
   * @param session
   * @param current_id
   * @return 1: 일치, 0: 불일치
   */
  @PostMapping(value="/id_check")
  @ResponseBody
  public String id_check(HttpSession session, @RequestBody String json_src) {
      System.out.println("-> json_src: " + json_src); // json_src: {"current_id":"userId"}
      
      JSONObject src = new JSONObject(json_src);
      String current_id = (String)src.get("current_id"); // 값 가져오기
      System.out.println("-> current_id: " + current_id);
      
      int memberno = (int)session.getAttribute("memberno"); // 세션에서 가져오기
      HashMap<String, Object> map = new HashMap<>();
      map.put("memberno", memberno);
      map.put("id", current_id);
      System.out.println("검증할 값: memberno: " + memberno + ", id: " + current_id);
      // ID 확인 로직
      int cnt = this.memberProc.id_check(map); // 현재 로그인한 사용자의 아이디 확인

      JSONObject json = new JSONObject();
      json.put("cnt", cnt); // 1: 아이디 일치, 0: 불일치
      System.out.println(json.toString());
      
      return json.toString();   
  }
  
  /**
   * 아이디 변경
   * @param session
   * @param model
   * @param current_id 현재 아이디
   * @param new_id 새로운 아이디
   * @return
   */
  @PostMapping(value="/id_update_proc")
  public String id_update_proc(HttpSession session, 
                                Model model, 
                                @RequestParam(value="current_id", defaultValue = "") String current_id, 
                                @RequestParam(value="new_id", defaultValue = "") String new_id) {
      
      if (this.memberProc.isMember(session)) {
          int memberno = (int)session.getAttribute("memberno"); // 세션에서 회원번호 가져오기
          
          HashMap<String, Object> map = new HashMap<>();
          map.put("memberno", memberno);
          map.put("id", current_id);
          
          // ID 일치 여부 확인
          int cnt = this.memberProc.id_check(map);
          
          if (cnt == 0) { // 아이디 불일치
              model.addAttribute("code", "id_not_equal");
              model.addAttribute("cnt", 0);
          } else { // 아이디 일치
              map = new HashMap<String, Object>();
              map.put("memberno", memberno);
              map.put("id", new_id); // 새로운 아이디
              System.out.println("-> new_id: " + new_id);
              // 아이디 변경
              int id_change_cnt = this.memberProc.id_update(map);
              System.out.println(id_change_cnt);
              if (id_change_cnt == 1) {
                  model.addAttribute("code", "id_change_success");
                  model.addAttribute("cnt", 1);
              } else {
                  model.addAttribute("code", "id_change_fail");
                  model.addAttribute("cnt", 0);
              }
          }

          return "/th/member/msg";   // 메시지 페이지로 리다이렉트
      } else {
          return "redirect:/th/member/login_cookie_need.html"; // 리다이렉션
      }
  }
  
}