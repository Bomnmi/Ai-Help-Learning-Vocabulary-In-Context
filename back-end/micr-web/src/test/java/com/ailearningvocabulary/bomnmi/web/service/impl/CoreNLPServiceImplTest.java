package com.ailearningvocabulary.bomnmi.web.service.impl;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/6 22:51
 */
@SpringBootTest
public class CoreNLPServiceImplTest {

    @Autowired
    StanfordCoreNLP pipeline;

    @Test
    void testLemmatization(){
        String text = "Joe Smith was born in California immersive. " +
                "In 2017, he went to Paris, France in the summer. " +
                "His flight left at 3:00pm on July 10th, 2017. " +
                "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
                "He sent a postcard to his sister Jane Smith. " +
                "After hearing about Joe's trip, Jane decided she might go to France one day.";
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);
        List<String> wordList = Arrays.asList("leave", "decide", "immersive");
        List<String> result = new ArrayList<>();
        // examples
        for (int i = 0; i < document.sentences().size(); i++) {
            CoreSentence coreSentence = document.sentences().get(i);
            List<String> lemmas = coreSentence.lemmas();
            List<String> sentences = coreSentence.tokensAsStrings();
            for (String word : wordList) {
                int index = lemmas.indexOf(word);
                if (index != -1) {
                    sentences.set(index, sentences.get(index) + "#");
                }
            }
            result.addAll(sentences);
            System.out.println(sentences);
            System.out.println(lemmas);
        }

        System.out.println(result);

    }
}
