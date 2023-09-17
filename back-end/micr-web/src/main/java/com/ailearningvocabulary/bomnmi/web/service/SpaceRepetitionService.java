package com.ailearningvocabulary.bomnmi.web.service;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 16:06
 */
public interface SpaceRepetitionService {

    UserVocabulary calculateEFAndNextReviewTime(UserVocabulary userVocabulary,Integer quality) throws NoSuchMethodException;
}
