package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.web.service.SpaceRepetitionService;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 16:07
 */
@Service("superMemo2SpaceRepetitionService")
public class SuperMemo2SpaceRepetitionServiceImpl implements SpaceRepetitionService {

    /**
     * This method is used to assess one repetition.
     * Can decide next review date for a vocabulary.
     * @param userVocabulary Record the info of review times, next review date etc
     * @param quality The quality of this repetition
     * @return Updated userVocabulary
     */
    @Override
    public UserVocabulary calculateEFAndNextReviewTime(UserVocabulary userVocabulary, Integer quality) {

        Double ef = userVocabulary.getEf();
        //User finish a review, but have not set into database, so here we add 1 to review times.
        int reviewTimes = userVocabulary.getReviewTimes() + 1;
        LocalDateTime nextReviewDate = userVocabulary.getNextReviewTime();
        LocalDateTime now = LocalDateTime.now();
        if(quality < 3){
            //unchanged ef, rememorize the word
            reviewTimes = 1;
            nextReviewDate = LocalDateTime.now();
            userVocabulary.setNextReviewTime(nextReviewDate);
            userVocabulary.setReviewTimes(reviewTimes);
            return userVocabulary;
        }

        if(reviewTimes == 1){
            nextReviewDate = now.plusDays(1L);
        } else if (reviewTimes == 2) {
            nextReviewDate = now.plusDays(6L);
        }else{
            //The duration between next review date and last review date is I(n - 1);
            long days = Duration.between(userVocabulary.getLastReviewTime(), nextReviewDate).toDays();
            days = (long) Math.floor(days * ef);
            //update the next review date
            nextReviewDate = now.plusDays(days);
        }

        ef = ef + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));

        //if ef < 1.3, then set ef be 1.3
        ef = ef < 1.3 ? 1.3 : ef;

        userVocabulary.setEf(ef);
        userVocabulary.setNextReviewTime(nextReviewDate);
        userVocabulary.setReviewTimes(reviewTimes);
        userVocabulary.setLastReviewTime(now);
        return userVocabulary;
    }
}
