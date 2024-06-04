package com.njwuqi.rollcall.mapper;
import com.njwuqi.rollcall.entity.Recordinfo;

public interface RecordinfoMapper {
    int insertRecordinfo(Recordinfo recordinfo);
    int updateRecordinfo(Recordinfo recordinfo);
    int delRecordinfo(long recordNumber);
    Recordinfo queryRecordinfoById(long recordNumber);
}
