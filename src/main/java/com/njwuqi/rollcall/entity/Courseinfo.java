package com.njwuqi.rollcall.entity;

public class Courseinfo {

  private long courseNumber;
  private String courseName;
  private long phone;
  private long state;
  private String addressName;

  private java.sql.Timestamp createTime;
  private Ruleinfo ruleinfo;
  private String realName;// 班级创建老师的姓名

  public long getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(long courseNumber) {
    this.courseNumber = courseNumber;
  }


  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public long getPhone() {
    return phone;
  }

  public void setPhone(long phone) {
    this.phone = phone;
  }

  public long getState() {
    return state;
  }

  public void setState(long state) {
    this.state = state;
  }


  public String getAddressName() {
    return addressName;
  }

  public void setAddressName(String addressName) {
    this.addressName = addressName;
  }


  public java.sql.Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }

  public Ruleinfo getRuleinfo() {
    return ruleinfo;
  }

  public void setRuleinfo(Ruleinfo ruleinfo) {
    this.ruleinfo = ruleinfo;
  }

  public String getRealName() {
    return realName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }
}
