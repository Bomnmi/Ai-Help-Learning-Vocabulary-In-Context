package com.ailearningvocabulary.bomnmi.web.view;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/16 11:05
 */
public class ResponseWordDefinition {

    private String translation;
    private Object definition;

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Object getDefinition() {
        return definition;
    }

    public void setDefinition(Object definition) {
        this.definition = definition;
    }
}
