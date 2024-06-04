package com.njwuqi.rollcall.entity;


public class Ruleinfo {

  private long ruleNumber;
  private String startTime;
  private String finalTime;
  private String remark;
  private long courseNumber;
  private double latitude;
  private double longitude;

  public long getRuleNumber() {
    return ruleNumber;
  }

  public void setRuleNumber(long ruleNumber) {
    this.ruleNumber = ruleNumber;
  }


  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }


  public String getFinalTime() {
    return finalTime;
  }

  public void setFinalTime(String finalTime) {
    this.finalTime = finalTime;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }


  public long getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(long courseNumber) {
    this.courseNumber = courseNumber;
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
