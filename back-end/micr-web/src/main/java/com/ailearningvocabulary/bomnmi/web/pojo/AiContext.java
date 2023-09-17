package com.ailearningvocabulary.bomnmi.web.pojo;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 0:56
 */
public class AiContext {

    private String title;
    private String text;

    @Override
    public String toString() {
        return "AiContext{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
