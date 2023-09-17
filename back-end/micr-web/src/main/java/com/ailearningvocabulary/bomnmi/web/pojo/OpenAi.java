package com.ailearningvocabulary.bomnmi.web.pojo;

import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 0:18
 */
public class OpenAi {

    private OpenAiService openAiService;
    private ChatMessage system;
    private ChatMessage user;

    public OpenAiService getOpenAiService() {
        return openAiService;
    }

    public void setOpenAiService(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    public ChatMessage getSystem() {
        return system;
    }

    public void setSystem(ChatMessage system) {
        this.system = system;
    }

    public ChatMessage getUser() {
        return user;
    }

    public void setUser(ChatMessage user) {
        this.user = user;
    }
}
