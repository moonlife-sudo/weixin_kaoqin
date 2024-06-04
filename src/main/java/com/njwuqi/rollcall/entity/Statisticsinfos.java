package com.njwuqi.rollcall.entity;


import java.util.List;

public class Statisticsinfos {
  private Courseinfo courseinfo;// 班级信息
  private List<String> absentInfos; // 学生学号+姓名+旷课次数 降序
  private List<String> lateInfos; // 学生学号+姓名+迟到次数 降序
  private List<String> earlyInfos; // 学生学号+姓名+早退次数 降序
  private List<String> leaveInfos; // 学生学号+姓名+请假次数 降序

  public Courseinfo getCourseinfo() {
    return courseinfo;
  }

  public void setCourseinfo(Courseinfo courseinfo) {
    this.courseinfo = courseinfo;
  }

  public List<String> getAbsentInfos() {
    return absentInfos;
  }

  public void setAbsentInfos(List<String> absentInfos) {
    this.absentInfos = absentInfos;
  }

  public List<String> getLateInfos() {
    return lateInfos;
  }

  public void setLateInfos(List<String> lateInfos) {
    this.lateInfos = lateInfos;
  }

  public List<String> getEarlyInfos() {
    return earlyInfos;
  }

  public void setEarlyInfos(List<String> earlyInfos) {
    this.earlyInfos = earlyInfos;
  }

  public List<String> getLeaveInfos() {
    return leaveInfos;
  }

  public void setLeaveInfos(List<String> leaveInfos) {
    this.leaveInfos = leaveInfos;
  }
}
