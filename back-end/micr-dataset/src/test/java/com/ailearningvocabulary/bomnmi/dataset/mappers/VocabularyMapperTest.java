package com.ailearningvocabulary.bomnmi.dataset.mappers;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class VocabularyMapperTest {

    @Autowired
    VocabularyMapper underTest;

    @Test
    void itShouldSelectVocabularyByWord() {
        //given
        String word = "accuse";
        Vocabulary vocabulary = new Vocabulary();
        vocabulary.setWord(word);

        underTest.insert(vocabulary);
        //when
        Vocabulary test = underTest.selectVocabularyByWord(word, word.toLowerCase());

        //then
        assertThat(test.getWord()).isEqualTo(vocabulary.getWord());
    }
}