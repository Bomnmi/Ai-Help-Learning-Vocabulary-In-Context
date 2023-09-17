package com.ailearningvocabulary.bomnmi.web.controller;

import com.ailearningvocabulary.bomnmi.api.model.Record;
import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/14 22:49
 */
@RestController
@RequestMapping("/v1/setting")
public class SettingController extends BaseController{


    @PostMapping("/update-learning-time")
    public ResponseResult updateLearningTime(@RequestParam("userId") String userId,
                                             @RequestParam("learningTime") String learningTime) {
        try{
            Record record = recordService.queryRecordByUserIdAndTime(userId, LocalDateTime.now());
            if (record == null) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            Integer learnedTime = record.getLearningTime() == null ? 0 : record.getLearningTime();
            record.setLearningTime(learnedTime + (int) Math.floor(Double.parseDouble(learningTime)));
            int rows = recordService.updateSelectiveRecord(record);
            if (rows != 1) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            return ResponseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

    }

    @GetMapping("/get-profile-detail")
    public ResponseResult getProfileDetail(@RequestParam("userId") String userId){
        try {
            Map<String, Object> resultMap = new HashMap<>();
            List<UserVocabulary> userVocabularies = userVocabularyService.queryAllUserVocabularyByUserId(userId);
            if (userVocabularies.size() == 0) {
                return ResponseResult.success();
            }
            List<UserVocabulary> todayLearnedWords = userVocabularyService.queryTodayLearnedWords(userVocabularies);
            resultMap.put("totalWords", userVocabularies.size());
            resultMap.put("todayLearnedWords", todayLearnedWords.size());
            User user = userService.queryUserById(userId);
            if (user == null) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            LocalDate createDate = user.getCreateTime().toLocalDate();
            resultMap.put("userJoinTime", ChronoUnit.DAYS.between(createDate, LocalDate.now()));

            List<Record> records = recordService.queryRecordsByUserId(userId);
            int overviewLearningTime = recordService.getOverviewLearningTime(records);

            //update record words
            Record record = recordService.queryRecordByUserIdAndTime(userId, LocalDateTime.now());
            record.setWordNumber(userVocabularies.size());
            int count = recordService.updateSelectiveRecord(record);
            if(count != 1){
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }

            resultMap.put("overviewLearningTime", overviewLearningTime);
            resultMap.put("todayLearningTime", recordService.getTodayLearningTime(records));

            return ResponseResult.success(resultMap);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @GetMapping("/get-all-words")
    public ResponseResult getAllWords(@RequestParam("userId") String userId) {

        try{
            List<UserVocabulary> userVocabularies = userVocabularyService.queryAllUserVocabularyByUserId(userId);
            return ResponseResult.success(userVocabularies);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @PostMapping("/skilled-word")
    public ResponseResult skilledWord(@RequestBody List<UserVocabulary> userVocabularies) {
        //Construct a skilled word
        for (UserVocabulary userVocabulary : userVocabularies) {
            userVocabulary.setNextReviewTime(LocalDateTime.of(2999, 12, 30, 0, 0, 0));
            userVocabulary.setLastReviewTime(LocalDateTime.of(2999, 12, 30, 0, 0, 0));
        }
        try{
            int rows = userVocabularyService.updateBatchUserVocabulary(userVocabularies);
            if(rows != 1){
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            return ResponseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }
    @PostMapping("/relearn-word")
    public ResponseResult relearnWord(@RequestBody List<UserVocabulary> userVocabularies) {
        //Construct a relearning word
        for (UserVocabulary userVocabulary : userVocabularies) {
            userVocabulary.setReviewTimes(0);
            userVocabulary.setEf(2.5d);
            userVocabulary.setLastReviewTime(LocalDateTime.now().minusDays(1));
            userVocabulary.setNextReviewTime(LocalDateTime.now());
        }
        try{
            int rows = userVocabularyService.updateBatchUserVocabulary(userVocabularies);
            if(rows != 1){
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            return ResponseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @GetMapping("/get-record")
    public ResponseResult getRecord(@RequestParam("userId") String userId){
        try{
            List<Record> records = recordService.queryRecordsByUserId(userId);
            return ResponseResult.success(records);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(e.getMessage());
        }
    }


}
