package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Record;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    Record selectByUserIdAndLoginTime(@Param("userId") String userId,
                                  @Param("loginTime") LocalDateTime loginTime);

    List<Record> selectRecordsByUserId(@Param("userId") String userId);
}