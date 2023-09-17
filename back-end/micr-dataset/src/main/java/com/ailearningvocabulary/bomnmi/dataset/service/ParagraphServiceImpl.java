package com.ailearningvocabulary.bomnmi.dataset.service;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.api.service.ParagraphService;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.ParagraphVocabularyMapper;
import com.ailearningvocabulary.bomnmi.dataset.mappers.UserVocabularyMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 13:36
 */
@DubboService(interfaceClass = ParagraphService.class, version = "1.0")
public class ParagraphServiceImpl implements ParagraphService {
    @Autowired
    ParagraphMapper paragraphMapper;

    @Autowired
    ParagraphVocabularyMapper paragraphVocabularyMapper;

    @Autowired
    UserVocabularyMapper userVocabularyMapper;



    @Override
    public int addParagraph(List<Paragraph> paragraphs) {

        return paragraphMapper.insertBatchParagraphs(paragraphs);
    }

    @Transactional
    @Override
    public void addParagraphAndVocabulary(List<Paragraph> paragraphs, List<List<String>> contentRequest, String userId) {
        try{
            //Add paragraphs into database
            int addParagraphRows = addParagraph(paragraphs);
            if (addParagraphRows != paragraphs.size()) {
                throw new ProjectException("5000", "Occur error when adding paragraph");
            }
            //Add the corresponding vocabularies of paragraph into database
            //If does not provide any of the words, skip
            if (contentRequest.size() != 0) {
                for (int i = 0; i < paragraphs.size(); i++) {
                    int addParagraphVocabularyRows = paragraphVocabularyMapper
                            .insertWordsOfOneParagraph(paragraphs.get(i).getId()
                                    , contentRequest.get(i));

                    //Update vocabulary, change their used to be 1, means there are using
                    List<UserVocabulary> userVocabularies = new ArrayList<>();
                    for (String word : contentRequest.get(i)) {
                        UserVocabulary userVocabulary = new UserVocabulary();
                        userVocabulary.setUserId(userId);
                        userVocabulary.setWord(word);
                        userVocabulary.setUsed(1);
                        userVocabularies.add(userVocabulary);
                    }
                    userVocabularyMapper.updateBatchUserVocabulary(userVocabularies);

                    if (addParagraphVocabularyRows != contentRequest.get(i).size()) {
                        throw new ProjectException("5000", "Occur error when adding paragraph vocabulary");
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Paragraph> queryUnReadParagraphsByUserId(String userId) {
        return paragraphMapper.selectUnReadParagraphByUserId(userId);
    }

    @Override
    public Paragraph queryParagraphByParagraphId(String paragraphId) {
        return paragraphMapper.selectParagraphByParagraphId(paragraphId);
    }

    @Override
    @Transactional
    public void updateParagraphStatusAndUserVocabularyStatus(String userId, String paragraphId) {
        try{
            Paragraph paragraph = new Paragraph();
            paragraph.setId(paragraphId);
            paragraph.setReadStatus(1);
            int rows = paragraphMapper.updateSelectiveParagraph(paragraph);
            if (rows != 1) {
                throw new ProjectException(ResultCode.SYSTEM_INNER_ERROR);
            }
            List<String> words = paragraphVocabularyMapper.selectWordsByParagraphId(paragraphId);

            if(words.size() != 0){
                //convert words to UserVocabularies
                List<UserVocabulary> userVocabularies = new ArrayList<>();

                for (String word : words) {
                    UserVocabulary userVocabulary = new UserVocabulary();
                    userVocabulary.setWord(word);
                    userVocabulary.setUserId(userId);
                    userVocabulary.setUsed(0);
                    userVocabularies.add(userVocabulary);
                }
                rows = userVocabularyMapper.updateBatchUserVocabulary(userVocabularies);
            }


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }
}
