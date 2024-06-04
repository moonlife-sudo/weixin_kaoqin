package com.njwuqi.rollcall.dto;


public class StatisticsinfoDisplay {
  private String date;//日期2023-07-25
  private String rule;//规则15:49-15:59
  private long totalNumber;
  private long attendanceNumber;
  private long leaveNumber;
  private long lateNumber;
  private long absentNumber;
  private long earlyNumber;
  private String earlyInfos;
  private String leaveInfos;
  private String lateInfos;
  private String absentInfos;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getRule() {
    return rule;
  }

  public void setRule(String rule) {
    this.rule = rule;
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
}
