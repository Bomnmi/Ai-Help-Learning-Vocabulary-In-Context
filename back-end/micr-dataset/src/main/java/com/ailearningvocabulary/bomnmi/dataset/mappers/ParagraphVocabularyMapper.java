package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.ParagraphVocabulary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParagraphVocabularyMapper {
    int deleteByPrimaryKey(ParagraphVocabulary key);

    int insert(ParagraphVocabulary record);

    int insertSelective(ParagraphVocabulary record);

    int insertWordsOfOneParagraph(@Param("paragraphId") String paragraphId,@Param("wordList") List<String> wordList);

    List<String> selectWordsByParagraphId(@Param("paragraphId") String paragraphId);

    List<String> selectUnReviewedWordByParagraphId(@Param("paragraphId") String paragraphId);

}