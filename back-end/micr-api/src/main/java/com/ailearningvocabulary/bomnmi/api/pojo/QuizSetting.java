package com.ailearningvocabulary.bomnmi.api.pojo;

import java.io.Serializable;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/20 14:48
 */
public class QuizSetting implements Serializable {

    private String datasetSetting;
    private String patternSetting;
    private Integer numberOfWords;

    QuizSetting(String datasetSetting, String patternSetting, Integer numberOfWords){
        this.datasetSetting = datasetSetting;
        this.patternSetting = patternSetting;
        this.numberOfWords = numberOfWords;
    }

    public Integer getNumberOfWords() {
        return numberOfWords;
    }

    public String getDatasetSetting(){
        return this.datasetSetting;
    }

    public String getPatternSetting(){
        return this.patternSetting;
    }

    public void setDatasetSetting(String datasetSetting) {
        this.datasetSetting = datasetSetting;
    }

    public void setPatternSetting(String patternSetting) {
        this.patternSetting = patternSetting;
    }

    public void setNumberOfWords(Integer numberOfWords) {
        this.numberOfWords = numberOfWords;
    }
}
