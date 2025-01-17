package dev.mvc.commureply;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import dev.mvc.commu.CommuProcInter;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.JsonResult;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/commureply")
public class CommureplyCont {
  @Autowired
  @Qualifier("dev.mvc.commu.CommuProc")
  private CommuProcInter commuProc;
  
  @Autowired
  @Qualifier("dev.mvc.commureply.CommureplyProc")
  private CommureplyProcInter commureplyProc;
  
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc;
  
  public CommureplyCont() {
    System.out.println("-> CommureplyCont created.");
  }
  

  @PostMapping(value = "/create")
  public JsonResult create(@RequestBody Map<String, Object> requestData,
                                  @RequestParam(name="communo", defaultValue="0") int communo,HttpSession session) {
    String content = (String) requestData.get("content");
    communo = Integer.parseInt((String) requestData.get("communo"));

    Integer memberno = (Integer) session.getAttribute("memberno");
    
    // memberno가 없으면 로그인 정보가 없다고 처리
    if (memberno == null) {
        return new JsonResult(0, "로그인 정보가 없습니다.");
    }

    // contentno가 0이면 오류 메시지 처리
    if (communo == 0) {
        return new JsonResult(0, "유효한 communo가 아닙니다.");
    }
    // ReplyVO 객체 생성 및 값 설정
    CommureplyVO commureplyVO = new CommureplyVO();
    commureplyVO.setContent(content);
    commureplyVO.setCommuno(communo);
    commureplyVO.setMemberno(memberno);

    // 댓글 등록 처리
    int cnt = commureplyProc.create(commureplyVO);

    // 결과에 따라 성공/실패 메시지 반환
    return new JsonResult(cnt > 0 ? 1 : 0, cnt > 0 ? "댓글 등록 성공" : "댓글 등록 실패");
  }
  
  /**
  컨텐츠별 댓글 목록 
  * http://localhost:9093/commureply/list_by_communo_join?communo=2
  * @param communo
  * @return
  */
 @GetMapping(value="/list_by_communo_join")
 @ResponseBody
 public String list_by_communo_join(@RequestParam(name="communo", defaultValue="0") int communo) {
   List<CommureplyMemberVO> list = commureplyProc.list_by_communo_join(communo);
   
   JSONObject obj = new JSONObject();
   obj.put("res", list);
   
   System.out.println("-> obj.toString(): " + obj.toString());
   
   return obj.toString();     
 }

}