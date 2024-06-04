package com.njwuqi.rollcall.entity;


public class Recordinfo {

  private long recordNumber;
  private String starttime;
  private String endtime;
  private long state1;
  private long state2;
  private long modifystate;
  private long phone;
  private long courseNumber;
  private long ruleNumber;
  private String rulestarttime;
  private String ruleendtime;
  private double latitude;
  private double longitude;
  public long getRecordNumber() {
    return recordNumber;
  }

  public void setRecordNumber(long recordNumber) {
    this.recordNumber = recordNumber;
  }

  public long getPhone() {
    return phone;
  }

  public void setPhone(long phone) {
    this.phone = phone;
  }

  public String getStarttime() {
    return starttime;
  }

  public void setStarttime(String starttime) {
    this.starttime = starttime;
  }

  public String getEndtime() {
    return endtime;
  }

  public void setEndtime(String endtime) {
    this.endtime = endtime;
  }

  public long getState1() {
    return state1;
  }

  public void setState1(long state1) {
    this.state1 = state1;
  }

  public long getState2() {
    return state2;
  }

  public void setState2(long state2) {
    this.state2 = state2;
  }

  public long getModifystate() {
    return modifystate;
  }

  public void setModifystate(long modifystate) {
    this.modifystate = modifystate;
  }

  public long getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(long courseNumber) {
    this.courseNumber = courseNumber;
  }

  public long getRuleNumber() {
    return ruleNumber;
  }

  public void setRuleNumber(long ruleNumber) {
    this.ruleNumber = ruleNumber;
  }

  public String getRulestarttime() {
    return rulestarttime;
  }

  public void setRulestarttime(String rulestarttime) {
    this.rulestarttime = rulestarttime;
  }

  public String getRuleendtime() {
    return ruleendtime;
  }

  public void setRuleendtime(String ruleendtime) {
    this.ruleendtime = ruleendtime;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}
