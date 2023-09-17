package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Record;
import com.ailearningvocabulary.bomnmi.api.service.RecordService;
import com.ailearningvocabulary.bomnmi.dataset.mappers.RecordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/14 22:24
 */

@DubboService(interfaceClass = RecordService.class, version = "1.0")
public class RecordServiceImpl implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    @Override
    public int addRecord(Record record) {
        return recordMapper.insert(record);
    }

    @Override
    public Record queryRecordByUserIdAndTime(String userId, LocalDateTime loginTime) {
        return recordMapper.selectByUserIdAndLoginTime(userId, loginTime);
    }

    @Override
    public int updateSelectiveRecord(Record record) {
        return recordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<Record> queryRecordsByUserId(String userId) {
        return recordMapper.selectRecordsByUserId(userId);
    }

    @Override
    public int getOverviewLearningTime(List<Record> records) {
        int result = 0;
        for (Record record : records) {
            Integer learningTime = record.getLearningTime();
            learningTime = learningTime == null ? 0 : learningTime;
            result += learningTime;
        }
        return result;
    }

    @Override
    public int getTodayLearningTime(List<Record> records) {
        for (Record record : records) {
            Integer learningTime = record.getLearningTime();
            learningTime = learningTime == null ? 0 : learningTime;
            if (record.getLoginTime().toLocalDate().compareTo(LocalDate.now()) == 0) {
                return learningTime;
            }
        }
        return -1;
    }

}
