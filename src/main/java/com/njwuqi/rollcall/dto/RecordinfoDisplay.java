package com.njwuqi.rollcall.dto;


public class RecordinfoDisplay {
  // 以日期降序、规则降序
  private String date;//日期2023-07-25
  private String rule;//规则15:49-15:59
  private String starttime;//上课打卡时间 15:47:05没有--
  private String endtime;// 下课打卡时间 15:47:05没有--
  private String state; // 正常 迟到 ..

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

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }
}
