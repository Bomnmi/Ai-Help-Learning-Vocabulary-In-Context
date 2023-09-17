package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.web.service.NLPService;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/7 0:16
 */
@Service("coreNLPService")
public class CoreNLPServiceImpl implements NLPService {

    @Autowired
    StanfordCoreNLP pipeline;

    @Override
    public String markAddedVocabularyInParagraph(String content, List<String> wordList) {
        CoreDocument document = new CoreDocument(content);

        // annotate the document
        pipeline.annotate(document);

        StringBuilder result = new StringBuilder();

        List<String> lemmatizationWordList = getLemmatizationWordList(wordList);
        List<String> wordListTemp = new ArrayList<>(lemmatizationWordList);

        //mark the word in context, add '#' at the end of the word
        for (int i = 0; i < document.sentences().size(); i++) {
            CoreSentence coreSentence = document.sentences().get(i);
            List<String> lemmas = coreSentence.lemmas();
            List<String> sentences = coreSentence.tokensAsStrings();
            for (String word : lemmatizationWordList) {
                int index = lemmas.indexOf(word);
                if (index != -1) {
                    wordListTemp.remove(word);
                    sentences.set(index, sentences.get(index) + "#");
                }
            }
            for (String str : sentences) {
                result.append(str);
                result.append(" ");
            }
        }
        if(wordListTemp.size() != 0){
            for (String word : wordListTemp) {
                wordList.remove(word);
            }
        }
        return result.toString();
    }

    @Override
    public List<String> getLemmatizationWordList(List<String> origin) {
        List<String> result = new ArrayList<>();
        for (String s : origin) {
            CoreDocument coreDocument = new CoreDocument(s);
            pipeline.annotate(coreDocument);
            result.add(coreDocument.tokens().get(0).lemma());
        }
        return result;
    }

    @Override
    public String getLemmatizationWord(String origin) {
        CoreDocument coreDocument = new CoreDocument(origin);
        pipeline.annotate(coreDocument);
        return coreDocument.tokens().get(0).lemma();
    }
}
