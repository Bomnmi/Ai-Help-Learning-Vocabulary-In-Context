package com.ailearningvocabulary.test;

import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.common.utils.HttpUtil;
import com.ailearningvocabulary.bomnmi.common.utils.ReflectUtil;
import com.ailearningvocabulary.test.pojo.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;
import edu.mit.jwi.morph.WordnetStemmer;
import javafx.geometry.Pos;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/2 18:28
 */

public class testReflectUtil {

    private POS[] POS_ARRAY = {POS.ADJECTIVE, POS.ADVERB, POS.NOUN, POS.VERB};


    @Test
    public void testRandom(){
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(4));
        }
    }

    @Test
    public void testReflect() throws InvocationTargetException, IllegalAccessException {
        User user = new User();
        user.setAddress("123");

        Map<String, Object> stringObjectMap = ReflectUtil.parseObjectToMap(user);

        System.out.println(stringObjectMap);
    }

    @Test
    public void testGenerateUUID(){
        System.out.println(CommonUtil.generateUUID());

    }

    @Test
    public void testSliceArr(){
        List<String> list = Arrays.asList("1", "2", "3", "4","5","6","7","8");
        List<List<String>> result = CommonUtil.sliceArray(list, 6);
        System.out.println(result);
    }

    @Test
    public void testDuration(){
        LocalDateTime dateTime1 = LocalDateTime.of(2023, 8, 5, 12, 0, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2023, 8, 5, 14, 30, 0);

        Duration duration = Duration.between(dateTime1, dateTime2);

        System.out.println(duration.toDays());

    }

    @Test
    public void testDoPost() throws IOException {
        String json = "{\"text\":[\"mouse\"],\"target_lang\":\"ZH\"}";
        String s = HttpUtil.doPost("https://api-free.deepl.com/v2/translate",
                "DeepL-Auth-Key 520827e9-d840-f6ae-fd96-ab0f2ceddb21:fx",
                "application/json", json);
        System.out.println(s);
    }

    @Test
    public void testDoGet() throws IOException {
        String s = HttpUtil.doGet("https://api.dictionaryapi.dev/api/v2/entries/en/hello");
        System.out.println(s);
    }
    private IIndexWord[] getAllPOSForBaseWord(String baseWord, IDictionary dict) {
        IIndexWord[] returnValue = new IIndexWord[4];
        for (int i = 0; i < returnValue.length; i++) {
            returnValue[i] = dict.getIndexWord(baseWord, POS_ARRAY[i]);
        }
        return returnValue;
    }

    @Test
    public void testJwi() throws IOException {
        String path = "C:\\Program Files (x86)\\WordNet\\2.1\\dict";
        URL url = new URL("file", null, path);
        IDictionary dict = new Dictionary(url);
        dict.open();
        List<String> synonymList = new ArrayList<>();
        WordnetStemmer stemmer = new WordnetStemmer(dict);
        String baseWord = "good";
        List<String> stems = stemmer.findStems(baseWord, POS.ADJECTIVE);
        if (!stems.isEmpty()) {
            String stem = stems.get(0); // Get the first stem
            System.out.println("Stem: " + stem);
            // Look up the synsets for the stem
            IIndexWord indexWord = dict.getIndexWord(stem, POS.NOUN);
            if (indexWord != null) {
                List<IWordID> wordIDs = indexWord.getWordIDs();
                for (IWordID synsetId : wordIDs) {
                    IWord synset = dict.getWord(synsetId);
                    System.out.println("Synset: " + synset.getSynset());
                }
            } else {
                System.out.println("Word not found in WordNet.");
            }
        }

    }

    @Test
    public void testGpt(){
        String token = "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";
        List<ChatMessage> list = new ArrayList<>();
        ChatMessage system = new ChatMessage("system", "You are a Vocabulary Translation Software");
        ChatMessage user = new ChatMessage("user", "\n" +
                "Give me the Chinese translations and part of speech of word 'mouse', only response json form:{meanings:[{meaning:[''], pos:'adj'},]}");
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
    public void testParseLocalDateTimeToDate(){
        String s = CommonUtil.parseLocalDateTimeToDate(LocalDateTime.now());
        System.out.println(s);
    }

}
