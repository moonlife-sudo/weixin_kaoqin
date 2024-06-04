package com.njwuqi.rollcall.dto;

// 班级号、学生手机号、规则号
public class TeacherUpdateStudentLeavedto {
    private long courseNumber;
    private long phone;
    private long ruleNumber;

    public long getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(long courseNumber) {
        this.courseNumber = courseNumber;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public long getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(long ruleNumber) {
        this.ruleNumber = ruleNumber;
    }
}
