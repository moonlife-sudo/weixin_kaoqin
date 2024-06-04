package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Electiveinfo;

public interface ElectiveinfoMapper {
    int insertElectiveinfo(Electiveinfo electiveinfo);
    int delElectiveinfo(long id);
    int updateElectiveinfo(Electiveinfo electiveinfo);
    Electiveinfo queryElectiveinfoById(long id);
    Electiveinfo queryElectiveinfoByphonecourseNumber(Electiveinfo electiveinfo);
}
