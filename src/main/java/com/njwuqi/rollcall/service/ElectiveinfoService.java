package com.njwuqi.rollcall.service;


import com.njwuqi.rollcall.entity.Electiveinfo;

public interface ElectiveinfoService {
    int insertElectiveinfo(Electiveinfo electiveinfo);
    int delElectiveinfo(long id);
    int updateElectiveinfo(Electiveinfo electiveinfo);
    Electiveinfo queryElectiveinfoById(long id);
    Electiveinfo queryElectiveinfoByphonecourseNumber(Electiveinfo electiveinfo);
}
