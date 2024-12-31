package dev.mvc.survey_item;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.mvc.survey_topic.SurveytopicVO;



@Service("dev.mvc.survey_item.SurveyitemProc")
public class SurveyitemProc implements SurveyitemProcInter{
  @Autowired
  private SurveyitemDAOInter surveyitemDAO;
  
  @Override
  public int create(SurveyitemVO surveyitemVO) {
    int cnt = this.surveyitemDAO.create(surveyitemVO);
    return cnt;
  }

  @Override
  public SurveyitemVO read(int surveyitemno) {
    SurveyitemVO surveyitemVO = this.surveyitemDAO.read(surveyitemno);
    return surveyitemVO;
  }

  @Override
  public int update(SurveyitemVO surveyitemVO) {
    int cnt = this.surveyitemDAO.update(surveyitemVO);
    return cnt;
  }

  @Override
  public int delete(int surveyitemno) {
    int cnt = this.surveyitemDAO.delete(surveyitemno);
    return cnt;
  }

  @Override
  public int increaseitemCnt(int surveyitemno) {
    int cnt = this.surveyitemDAO.increaseitemCnt(surveyitemno);
    return cnt;
  }

  @Override
  public ArrayList<SurveyitemVO> listBySurveytopicno(int surveytopicno) {
    ArrayList<SurveyitemVO> list = this.surveyitemDAO.listBySurveytopicno(surveytopicno);
    return list;
  }



}
