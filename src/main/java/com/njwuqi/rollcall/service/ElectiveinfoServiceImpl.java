package com.njwuqi.rollcall.service;

import com.njwuqi.rollcall.entity.Electiveinfo;
import com.njwuqi.rollcall.mapper.ElectiveinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ElectiveinfoServiceImpl implements ElectiveinfoService {
    @Autowired
   private ElectiveinfoMapper electiveinfoMapper;

    @Override
    public int insertElectiveinfo(Electiveinfo electiveinfo) {
        return electiveinfoMapper.insertElectiveinfo(electiveinfo);
    }

    @Override
    public int delElectiveinfo(long id) {
        return electiveinfoMapper.delElectiveinfo(id);
    }

    @Override
    public int updateElectiveinfo(Electiveinfo electiveinfo) {
        return electiveinfoMapper.updateElectiveinfo(electiveinfo);
    }

    @Override
    public Electiveinfo queryElectiveinfoById(long id) {
        return electiveinfoMapper.queryElectiveinfoById(id);
    }

    @Override
    public Electiveinfo queryElectiveinfoByphonecourseNumber(Electiveinfo electiveinfo) {
        return electiveinfoMapper.queryElectiveinfoByphonecourseNumber(electiveinfo);
    }
}
