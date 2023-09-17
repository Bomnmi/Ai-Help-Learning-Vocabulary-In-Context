package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import com.ailearningvocabulary.bomnmi.api.service.UserVocabularyService;
import com.ailearningvocabulary.bomnmi.api.service.VocabularyService;
import com.ailearningvocabulary.bomnmi.web.service.QuizPageService;
import com.ailearningvocabulary.bomnmi.web.view.ResponseQuizProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/22 14:09
 */
@Service
public class QuizPageServiceImpl implements QuizPageService {
    @Autowired
    UserVocabularyService userVocabularyService;

    @Autowired
    VocabularyService vocabularyService;

    @Value("${ai.quiz-setting.distract-options}")
    int distractOptions;

    @Override
    public List<ResponseQuizProblem> generateQuizProblem(String userId, QuizSetting quizSetting) {

        //First need to select the word that satisfy the quiz setting
        List<UserVocabulary> quizVocabularies = userVocabularyService.queryWordsByQuizSetting(userId, quizSetting);

        //Then, select the common words from t_vocabulary_common, to be the distract option.
        List<String> wordList = quizVocabularies.stream().map(UserVocabulary::getWord).collect(Collectors.toList());
        List<Vocabulary> distractVocabularies = vocabularyService.
                queryLimitCommonVocabularyExcludeSomeWords(distractOptions * wordList.size(), wordList);

        //Combine the distractVocabularies and quizVocabularies to be a quiz problem
        List<ResponseQuizProblem> result = new ArrayList<>();
        int tempIndex = 0;

        for (UserVocabulary userVocabulary : quizVocabularies) {
            ResponseQuizProblem tempQuizProblem = new ResponseQuizProblem();
            tempQuizProblem.setProblemDefinition(userVocabulary.getDefinition());
            tempQuizProblem.setCorrectWord(userVocabulary.getWord());
            tempQuizProblem.setAnswer("");

            Vocabulary correctVocabulary = new Vocabulary();
            correctVocabulary.setWord(userVocabulary.getWord());
            correctVocabulary.setDefinition(userVocabulary.getDefinition());
            correctVocabulary.setTranslation(userVocabulary.getTranslation());

            List<Vocabulary> tempVocabularyList = new ArrayList<>();

            int insertIndex = new Random().nextInt(4);
            for (int i = 0; i < 4; i++) {
                if (insertIndex == i) {
                    tempVocabularyList.add(correctVocabulary);
                }else{
                    tempVocabularyList.add(distractVocabularies.get(tempIndex));
                    tempIndex++;
                }
            }
            Collections.shuffle(tempVocabularyList);
            tempQuizProblem.setVocabularyList(tempVocabularyList);
            result.add(tempQuizProblem);
        }
        return result;
    }
}