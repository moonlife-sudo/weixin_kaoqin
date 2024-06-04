package com.njwuqi.rollcall.service;
import com.njwuqi.rollcall.entity.Recordinfo;
import com.njwuqi.rollcall.mapper.RecordinfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecordinfoServiceImpl implements RecordinfoService {
    @Autowired
   private RecordinfoMapper recordinfoMapper;

    @Override
    public int insertRecordinfo(Recordinfo recordinfo) {
        return recordinfoMapper.insertRecordinfo(recordinfo);
    }

    @Override
    public int delRecordinfo(long recordNumber) {
        return recordinfoMapper.delRecordinfo(recordNumber);
    }

    @Override
    public Recordinfo queryRecordinfoById(long recordNumber) {
        return recordinfoMapper.queryRecordinfoById(recordNumber);
    }

    @Override
    public int updateRecordinfo(Recordinfo recordinfo) {
        return recordinfoMapper.updateRecordinfo(recordinfo);
    }
}
