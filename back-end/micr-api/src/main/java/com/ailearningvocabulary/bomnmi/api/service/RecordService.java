package com.ailearningvocabulary.bomnmi.api.service;

import com.ailearningvocabulary.bomnmi.api.model.Record;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordService {

    int addRecord(Record record);

    Record queryRecordByUserIdAndTime(String userId, LocalDateTime loginTime);

    int updateSelectiveRecord(Record record);

    List<Record> queryRecordsByUserId(String userId);

    int getOverviewLearningTime(List<Record> records);

    int getTodayLearningTime(List<Record> records);
}
