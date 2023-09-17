package com.ailearningvocabulary.bomnmi.web.settings;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/6 22:54
 */
@Configuration
public class CoreNLPConfiguration {

    @Bean
    public StanfordCoreNLP constructPipeline(){
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        props.setProperty("ner.useSUTime", "false");
        // build pipeline
        return new StanfordCoreNLP(props);
    }

}
