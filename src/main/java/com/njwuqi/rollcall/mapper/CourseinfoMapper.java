package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Courseinfo;

public interface CourseinfoMapper {
    int insertCourseinfo(Courseinfo courseinfo);
    int delCourseinfo(long courseNumber);
    int updateCourseinfo(Courseinfo courseinfo);
    Courseinfo queryCourseinfoById(long courseNumber);
}
