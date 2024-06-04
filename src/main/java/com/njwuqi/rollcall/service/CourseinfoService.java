package com.njwuqi.rollcall.service;
import com.njwuqi.rollcall.entity.Courseinfo;

public interface CourseinfoService {
    int insertCourseinfo(Courseinfo courseinfo);
    int delCourseinfo(long courseNumber);
    int updateCourseinfo(Courseinfo courseinfo);
    Courseinfo queryCourseinfoById(long courseNumber);
}
