package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VocabularyMapper {
    int insert(Vocabulary record);

    int insertSelective(Vocabulary record);

    Vocabulary selectVocabularyByWord(@Param("word") String word, @Param("prefix") String prefix);

    List<Vocabulary> selectLimitCommonVocabularyExcludeSomeWords(@Param("wordNumber") Integer wordNumber,
                                                                 @Param("wordList") List<String> wordList);

    Vocabulary selectCommonVocabulary(@Param("word") String word);
}