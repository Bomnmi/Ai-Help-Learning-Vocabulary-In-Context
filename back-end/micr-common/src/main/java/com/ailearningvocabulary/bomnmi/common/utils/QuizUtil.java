package com.ailearningvocabulary.bomnmi.common.utils;

import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/20 14:59
 */
public class QuizUtil {

    public boolean compareQuizSetting(QuizSetting quizSetting1, QuizSetting quizSetting2) {
        return (quizSetting1.getDatasetSetting() == quizSetting2.getDatasetSetting()) &&
                (quizSetting1.getPatternSetting() == quizSetting2.getPatternSetting());
    }
}
