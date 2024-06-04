package com.njwuqi.rollcall.entity;


public class Statisticsinfo {

  private long statisticsNumber;
  private String date;
  private long courseNumber;
  private long totalNumber;
  private long attendanceNumber;
  private long leaveNumber;
  private long lateNumber;
  private long absentNumber;
  private long earlyNumber;
  private long ruleNumber;
  private String earlyInfos;
  private String leaveInfos;
  private String lateInfos;
  private String absentInfos;
  // 上课未打卡人数
  private int startNotCallNumber;
  // 下课未打卡人数
  private int endNotCallNumber;

  public long getStatisticsNumber() {
    return statisticsNumber;
  }

  public void setStatisticsNumber(long statisticsNumber) {
    this.statisticsNumber = statisticsNumber;
  }


  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }


  public long getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(long courseNumber) {
    this.courseNumber = courseNumber;
  }


  public long getTotalNumber() {
    return totalNumber;
  }

  public void setTotalNumber(long totalNumber) {
    this.totalNumber = totalNumber;
  }


  public long getAttendanceNumber() {
    return attendanceNumber;
  }

  public void setAttendanceNumber(long attendanceNumber) {
    this.attendanceNumber = attendanceNumber;
  }


  public long getLeaveNumber() {
    return leaveNumber;
  }

  public void setLeaveNumber(long leaveNumber) {
    this.leaveNumber = leaveNumber;
  }


  public long getLateNumber() {
    return lateNumber;
  }

  public void setLateNumber(long lateNumber) {
    this.lateNumber = lateNumber;
  }


  public long getAbsentNumber() {
    return absentNumber;
  }

  public void setAbsentNumber(long absentNumber) {
    this.absentNumber = absentNumber;
  }


  public long getEarlyNumber() {
    return earlyNumber;
  }

  public void setEarlyNumber(long earlyNumber) {
    this.earlyNumber = earlyNumber;
  }


  public long getRuleNumber() {
    return ruleNumber;
  }

  public void setRuleNumber(long ruleNumber) {
    this.ruleNumber = ruleNumber;
  }

  public String getEarlyInfos() {
    return earlyInfos;
  }

  public void setEarlyInfos(String earlyInfos) {
    this.earlyInfos = earlyInfos;
  }

  public String getLeaveInfos() {
    return leaveInfos;
  }

  public void setLeaveInfos(String leaveInfos) {
    this.leaveInfos = leaveInfos;
  }

  public String getLateInfos() {
    return lateInfos;
  }

  public void setLateInfos(String lateInfos) {
    this.lateInfos = lateInfos;
  }

  public String getAbsentInfos() {
    return absentInfos;
  }

  public void setAbsentInfos(String absentInfos) {
    this.absentInfos = absentInfos;
  }

  public int getStartNotCallNumber() {
    return startNotCallNumber;
  }

  public void setStartNotCallNumber(int startNotCallNumber) {
    this.startNotCallNumber = startNotCallNumber;
  }

  public int getEndNotCallNumber() {
    return endNotCallNumber;
  }

  public void setEndNotCallNumber(int endNotCallNumber) {
    this.endNotCallNumber = endNotCallNumber;
  }
}
