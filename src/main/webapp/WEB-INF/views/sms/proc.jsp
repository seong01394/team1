<%@ page language="java" import="java.util.*, java.util.Base64.*, java.security.*, java.io.*, java.net.*" %>
<%!
/**==============================================================
  Description        :  사용 함수 선언
==============================================================**/
 /**
 * nullcheck
 * @param str, Defaultvalue
 * @return
 */

 public static String nullcheck(String str,  String Defaultvalue ) throws Exception
 {
      String ReturnDefault = "" ;
      if (str == null)
      {
          ReturnDefault =  Defaultvalue ;
      }
      else if (str == "" )
     {
          ReturnDefault =  Defaultvalue ;
      }
      else
      {
                  ReturnDefault = str ;
      }
       return ReturnDefault ;
 }
 /**
 * BASE64 Encoder
 * @param str
 * @return
 */
public static String base64Encode(String str)  throws java.io.IOException {
    // sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    // byte[] strByte = str.getBytes();
    // String result = encoder.encode(strByte);
    // return result ;
    
    String result = Base64.getEncoder().encodeToString(str.getBytes());
    return result;
}

/**
 * BASE64 Decoder
 * @param str
 * @return
 */
public static String base64Decode(String str)  throws java.io.IOException {
    // sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    // byte[] strByte = decoder.decodeBuffer(str);
    // String result = new String(strByte);
    // return result ;
    
    byte[] result = Base64.getDecoder().decode(str);
    return new String(result);
}
%>
<%
/**==============================================================
  Description        : 캐릭터셋 정의
  EUC-KR: @ page contentType="text/html;charset=EUC-KR
  UTF-8: @ page contentType="text/html;charset=UTF-8
==============================================================**/
%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
/**==============================================================
  Description        :  사용자 샘플코드
==============================================================**/
String charsetType = "UTF-8"; //EUC-KR 또는 UTF-8

request.setCharacterEncoding(charsetType);
response.setCharacterEncoding(charsetType);
String  action     = nullcheck(request.getParameter("action"), "");
if(action.equals("go")) {

    String sms_url = "";
    sms_url = "https://sslsms.cafe24.com/sms_sender.php"; // SMS 전송요청 URL
    String user_id = base64Encode("itwillsms"); // SMS 아이디
    String secure = base64Encode("2a828056a1882665c31fc8538c56c1bf"); // 인증키
    
    // form.jsp에서 보낸 문자열을 사용
    // String msg = base64Encode(nullcheck(request.getParameter("msg"), ""));
    
    // Spring SMSCont.java 보낸 문자열을 사용
    String msg = (String)request.getAttribute("msg");
    msg = base64Encode(nullcheck(msg, ""));
            
    // System.out.println(" ->msg: " + msg);
    String rphone = base64Encode(nullcheck(request.getParameter("rphone"), ""));
    // System.out.println(" ->rphone: " + rphone);
    String rnickname = base64Encode(nullcheck(request.getParameter("rnickname"), ""));
    String sphone1 = base64Encode(nullcheck(request.getParameter("sphone1"), ""));
    String sphone2 = base64Encode(nullcheck(request.getParameter("sphone2"), ""));
    String sphone3 = base64Encode(nullcheck(request.getParameter("sphone3"), ""));
    String rdate = base64Encode(nullcheck(request.getParameter("rdate"), ""));
    String rtime = base64Encode(nullcheck(request.getParameter("rtime"), ""));
    String mode = base64Encode("1");
    String subject = "";
    if(nullcheck(request.getParameter("smsType"), "").equals("L")) {
        subject = base64Encode(nullcheck(request.getParameter("subject"), ""));
    }
    String testflag = base64Encode(nullcheck(request.getParameter("testflag"), ""));
    String destination = base64Encode(nullcheck(request.getParameter("destination"), ""));
    String repeatFlag = base64Encode(nullcheck(request.getParameter("repeatFlag"), ""));
    String repeatNum = base64Encode(nullcheck(request.getParameter("repeatNum"), ""));
    String repeatTime = base64Encode(nullcheck(request.getParameter("repeatTime"), ""));
    String returnurl = nullcheck(request.getParameter("returnurl"), "");
    String nointeractive = nullcheck(request.getParameter("nointeractive"), "");
    String smsType = base64Encode(nullcheck(request.getParameter("smsType"), ""));

    String[] host_info = sms_url.split("/");
    String host = host_info[2];
    String path = "/" + host_info[3];
    int port = 80;

    // 데이터 맵핑 변수 정의
    String arrKey[]
        = new String[] {"user_id","secure","msg", "rphone","sphone1","sphone2","sphone3","rdate","rtime"
                    ,"mode","testflag","destination","repeatFlag","repeatNum", "repeatTime", "smsType", "subject", "rnickname"};
    String valKey[]= new String[arrKey.length] ;
    valKey[0] = user_id;
    valKey[1] = secure;
    valKey[2] = msg;
    valKey[3] = rphone;
    valKey[4] = sphone1;
    valKey[5] = sphone2;
    valKey[6] = sphone3;
    valKey[7] = rdate;
    valKey[8] = rtime;
    valKey[9] = mode;
    valKey[10] = testflag;
    valKey[11] = destination;
    valKey[12] = repeatFlag;
    valKey[13] = repeatNum;
    valKey[14] = repeatTime;
    valKey[15] = smsType;
    valKey[16] = subject;
    valKey[17] = rnickname;
    
    String boundary = "";
    Random rnd = new Random();
    String rndKey = Integer.toString(rnd.nextInt(32000));
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] bytData = rndKey.getBytes();
    md.update(bytData);
    byte[] digest = md.digest();
    for(int i =0;i<digest.length;i++)
    {
        boundary = boundary + Integer.toHexString(digest[i] & 0xFF);
    }
    boundary = "---------------------"+boundary.substring(0,11);

    // 본문 생성
    String data = "";
    String index = "";
    String value = "";
    for (int i=0;i<arrKey.length; i++)
    {
        index =  arrKey[i];
        value = valKey[i];
        data +="--"+boundary+"\r\n";
        data += "Content-Disposition: form-data; name=\""+index+"\"\r\n";
        data += "\r\n"+value+"\r\n";
        data +="--"+boundary+"\r\n";
    }

    //out.println(data);

    InetAddress addr = InetAddress.getByName(host);
    Socket socket = new Socket(host, port);
    // 헤더 전송
    BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charsetType));
    wr.write("POST "+path+" HTTP/1.0\r\n");
    wr.write("Content-Length: "+data.length()+"\r\n");
    wr.write("Content-type: multipart/form-data, boundary="+boundary+"\r\n");
    wr.write("\r\n");

    // 데이터 전송
    wr.write(data);
    wr.flush();

    // 결과값 얻기
    BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(),charsetType));
    String line;
    String alert = "";
    ArrayList tmpArr = new ArrayList();
    while ((line = rd.readLine()) != null) {
        tmpArr.add(line);
    }
    wr.close();
    rd.close();

    String tmpMsg = (String)tmpArr.get(tmpArr.size()-1);
    String[] rMsg = tmpMsg.split(",");
    String Result= rMsg[0]; //발송결과
    String Count= ""; //잔여건수
    if(rMsg.length>1) {Count= rMsg[1]; }

                    //발송결과 알림
    if(Result.equals("success")) {
        alert = "성공적으로 발송하였습니다.";
        alert += " 잔여건수는 "+ Count+"건 입니다.";
    }
    else if(Result.equals("reserved")) {
        alert = "성공적으로 예약되었습니다";
        alert += " 잔여건수는 "+ Count+"건 입니다.";
    }
    else if(Result.equals("3205")) {
        alert = "잘못된 번호형식입니다.";
    }
    else {
        alert = "[Error]"+Result;
    }

    out.println(nointeractive);

    if(nointeractive.equals("1") && !(Result.equals("Test Success!")) && !(Result.equals("success")) && !(Result.equals("reserved")) ) {
       System.out.println("<script>alert('" + alert + "')</script>");
    } else if(!(nointeractive.equals("1"))) {
       out.println("<script>location.href='"+returnurl+"';</script>");
    }
}
%>