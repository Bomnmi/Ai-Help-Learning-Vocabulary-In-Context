package com.ailearningvocabulary.bomnmi.web.settings;

import com.ailearningvocabulary.bomnmi.web.pojo.OpenAi;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/5 0:15
 */
@Configuration
public class OpenAiConfiguration {
    @Value("${ai.chat-gpt.api-key}")
    private String apiKey;

    @Bean
    public OpenAi openAi(){
        OpenAi openAi = new OpenAi();
        //Need add a duration to avoid error: SocketTimeOut Exception
        openAi.setOpenAiService(new OpenAiService(apiKey, Duration.ofSeconds(50)));
        openAi.setSystem(new ChatMessage("system", "You are a English Teacher"));
        openAi.setUser(new ChatMessage("user"));
        return openAi;
    }
}
