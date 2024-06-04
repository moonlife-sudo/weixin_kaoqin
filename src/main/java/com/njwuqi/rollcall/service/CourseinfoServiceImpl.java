package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Courseinfo;
import com.njwuqi.rollcall.mapper.CourseinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CourseinfoServiceImpl implements CourseinfoService {
    @Autowired
   private CourseinfoMapper courseinfoMapper;

    @Override
    public int insertCourseinfo(Courseinfo courseinfo) {
        return courseinfoMapper.insertCourseinfo(courseinfo);
    }

    @Override
    public int delCourseinfo(long courseNumber) {
        return courseinfoMapper.delCourseinfo(courseNumber);
    }

    @Override
    public int updateCourseinfo(Courseinfo courseinfo) {
        return courseinfoMapper.updateCourseinfo(courseinfo);
    }

    @Override
    public Courseinfo queryCourseinfoById(long courseNumber) {
        return courseinfoMapper.queryCourseinfoById(courseNumber);
    }
}
