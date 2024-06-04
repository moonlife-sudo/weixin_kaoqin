package com.njwuqi.rollcall.service;
import com.njwuqi.rollcall.entity.Recordinfo;

public interface RecordinfoService {
    int insertRecordinfo(Recordinfo recordinfo);
    int updateRecordinfo(Recordinfo recordinfo);
    int delRecordinfo(long recordNumber);
    Recordinfo queryRecordinfoById(long recordNumber);
}
