package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.pojo.AiContext;
import com.ailearningvocabulary.bomnmi.web.pojo.AiContexts;
import com.ailearningvocabulary.bomnmi.web.pojo.OpenAi;
import com.ailearningvocabulary.bomnmi.web.service.AiService;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/4 19:08
 */
@Service("chatGptService")
public class ChatGptServiceImpl implements AiService {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${ai.chat-gpt.paragraph-type}")
    private String[] paragraphType;

    @Value("${ai.chat-gpt.token-limit}")
    private Integer tokenLimit;

    @Autowired
    OpenAi openAi;


    @Override
    public List<Paragraph> generateParagraphByWordList(List<List<String>> requestContent) throws ProjectException{
        StringBuffer stringBuffer = new StringBuffer("");
        int randomIndex = CommonUtil.getRandomNumberLessThenN(paragraphType.length);

        int contextNumber = requestContent.size() == 0 ? 1 : requestContent.size();
        stringBuffer.append("Could you give me ").append(contextNumber).append(" short ").append(paragraphType[randomIndex]);
        if (requestContent.size() != 0) {
            stringBuffer.append("s with these ").append(requestContent.size()).append(" group of words and each paragraph should has a title:");
            requestContent.forEach(list -> {
                stringBuffer.append("[");
                list.forEach(word -> {
                    stringBuffer.append(word);
                    stringBuffer.append(", ");
                });
                stringBuffer.append("],");
            });
        }else{
            stringBuffer.append(" with title?");
        }
        stringBuffer.append(" Your response should only be a json form like: " +
                "{\"contexts\":[{\"title\":\"\",\"text\":\"\"}]}.");

        List<ChatMessage> list = new ArrayList<>();
        openAi.getUser().setContent(stringBuffer.toString());

        list.add(openAi.getSystem());
        list.add(openAi.getUser());

        logger.info(list.toString());
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(list)
                .model("gpt-3.5-turbo")
                .maxTokens(tokenLimit)
                .build();

        List<Paragraph> result = new ArrayList<>();

        //Mock chatGpt return.
        /*ChatCompletionChoice temp = new ChatCompletionChoice();
        ChatMessage tempMessage = new ChatMessage();
        tempMessage.setContent("{\"contexts\":" +
                "[" +
                    "{" +
                        " \"title\":\"The Forgotten Tissue\"," +
                        "\"text\":\"As the cold winter air settled in, Jennifer reached into her pocket and discovered a crumpled tissue. She realized that it had been forgotten during her last trip to the park. With a nostalgic smile, she realized how much that tissue had endured, just like the memories it held within.\"" +
                    "}," +
                    "{" +
                        " \"title\":\"The Forgotten Tissue\"," +
                        "\"text\":\"As the cold winter air settled in, Jennifer reached into her pocket and discovered a crumpled tissue. She realized that it had been forgotten during her last trip to the park. With a nostalgic smile, she realized how much that tissue had endured, just like the memories it held within.\"" +
                    "}," +
                    "{" +
                        " \"title\":\"The Forgotten Tissue\"," +
                        "\"text\":\"As the cold winter air settled in, Jennifer reached into her pocket and discovered a crumpled tissue. She realized that it had been forgotten during her last trip to the park. With a nostalgic smile, she realized how much that tissue had endured, just like the memories it held within.\"" +
                    "}" +
                "]}");

        temp.setMessage(tempMessage);
        AiContexts aiContexts = null;
        try {
            aiContexts = decodeResponseMessage(temp);
            for (AiContext context : aiContexts.getContexts()) {
                Paragraph paragraph = new Paragraph();
                paragraph.setTitle(context.getTitle());
                paragraph.setContent(context.getText());
                result.add(paragraph);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }*/

        //Real chatGpt
        ChatCompletionResult chatCompletion = openAi.getOpenAiService().createChatCompletion(completionRequest);

        chatCompletion.getChoices().forEach(i -> {
            try {
                //Convert json response to pojo

                AiContexts aiContexts = decodeResponseMessage(i);
                for (AiContext context : aiContexts.getContexts()) {
                    Paragraph paragraph = new Paragraph();
                    paragraph.setTitle(context.getTitle());
                    paragraph.setContent(context.getText());
                    result.add(paragraph);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });



        return result;
    }


    /*
    Title: Working from Home

    Context 1:
    Every morning,
    Sarah sat down at her desk with a cup of coffee
    in her hand and her computer on her add.
    She loved the convenience of working remotely,
    as she could enjoy her beverage while staying connected to
    her colleagues and clients. With her eyes fixed on the screen
    through her glasses, she delved into her daily tasks, feeling grateful
    for the efficiency technology brought to her work life.
     */
    private AiContexts decodeResponseMessage(ChatCompletionChoice choice) throws JsonProcessingException {

        String jsonContent = choice.getMessage().getContent();
        logger.info(choice.getMessage().getContent());
        // use ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonContent, AiContexts.class);
    }
}
