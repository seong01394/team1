package dev.mvc.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("dev.mvc.calendar.CalendarProc")
public class CalendarProc implements CalendarProcInter {
  @Autowired
  private CalendarDAOInter calendarDAO;
  
  public CalendarProc() {
    System.out.println("-> CalendarProc created.");
  }
  
  /** 등록 */
  @Override
  public int create(CalendarVO calendarVO) {
    int cnt = this.calendarDAO.create(calendarVO);
    
    return cnt;
  }
  
  /** 목록 */
  @Override
  public ArrayList<CalendarVO> list_all() {
    ArrayList<CalendarVO> list = this.calendarDAO.list_all();
    
    return list;
  }
  
  /** 조회 */
  @Override
  public CalendarVO read(int calendarno) {
    CalendarVO calendarVO = this.calendarDAO.read(calendarno);
    return calendarVO;
  }
  
  /** 수정 */
  @Override
  public int update(CalendarVO calendarVO) {
    int cnt = this.calendarDAO.update(calendarVO);
    return cnt;
  }

  
  /** 삭제  */
  @Override
  public int delete(int calendarno) {
    int cnt = this.calendarDAO.delete(calendarno);
    return cnt;
  }

  @Override
  public ArrayList<CalendarVO> list_calendar(String date) {
    ArrayList<CalendarVO> list = this.calendarDAO.list_calendar(date);
    return list;
  }

  @Override
  public ArrayList<CalendarVO> list_calendar_day(String date) {
    ArrayList<CalendarVO> list = this.calendarDAO.list_calendar_day(date);
    return list;
  }    

  @Override
  public int increaseCnt(int calendarno) {
    int cnt = this.calendarDAO.increaseCnt(calendarno);
    return cnt;
  }
  
  @Override
  public int update_seqno_forward(int calendarno) {
    int cnt = this.calendarDAO.update_seqno_forward(calendarno);
    return cnt;
  }

  @Override
  public int update_seqno_backward(int calendarno) {
    int cnt = this.calendarDAO.update_seqno_backward(calendarno);
    return cnt;
  }
}