package com.ailearningvocabulary.bomnmi.web;

import com.ailearningvocabulary.bomnmi.web.service.EmailService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import edu.stanford.nlp.trees.WordNetConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class MicrWebApplicationTests {

    @Autowired
    EmailService emailService;
    @Test
    void contextLoads() {
        emailService.sendVerificationCode("624883617@qq.com");
    }

    @Test
    void chatGptTest(){
        String token = "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";
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage system = new ChatMessage("system", "You are a English Quiz Software");
        ChatMessage user = new ChatMessage("user", "\n" +
                "Help me generate 4 disturbances of word 'interruption' include Chinese, response as json");
        list.add(system);
        list.add(user);
        OpenAiService service = new OpenAiService(token);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(list)
                .model("gpt-3.5-turbo")
                .maxTokens(300)
                .build();

        service.createChatCompletion(completionRequest).getChoices().forEach(System.out::println);
    }

    @Test
    void testListLambda() {
        int wordsLimit = 4;
        List<String> wordList = Arrays.asList("123", "!25", "1251", "!2616", "asdasd", "agsgh", "zxgbxchnxchj", "sar q");
        List<List<String>> requestContent = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (int i = 1; i < wordList.size() + 1; i++) {
            tempList.add(wordList.get(i - 1));
            if (i % wordsLimit == 0 || i == wordList.size()) {
                requestContent.add(tempList);
                tempList = new ArrayList<>();
            }
            if (requestContent.size() == 3) {
                break;
            }
        }
        StringBuffer stringBuffer = new StringBuffer("");
        if (requestContent.size() != 0) {
            stringBuffer.append("with these " + requestContent.size() + " group of words: ");
            requestContent.forEach(list -> {
                stringBuffer.append("[");
                list.forEach(word -> {
                    stringBuffer.append(word);
                    stringBuffer.append(", ");
                });
                stringBuffer.append("]");
            });
        }
        System.out.println(stringBuffer);

    }



}
