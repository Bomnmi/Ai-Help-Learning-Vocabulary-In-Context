package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.common.utils.HttpUtil;
import com.ailearningvocabulary.bomnmi.web.service.TranslationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/10 16:56
 */
@Service("deepLTranslationService")
public class DeepLTranslationServiceImpl implements TranslationService {
    @Value("${ai.deepL.url}")
    private String url;

    @Value("${ai.deepL.api-key}")
    private String apiKey;


    @Override
    public String queryTranslationOrDefinition(String word) throws IOException {
        return null;
    }

    @Override
    public String queryTranslationOfParagraph(String content) throws IOException {
        String json = "{\"text\":[\"" + content + "\"],\"target_lang\":\"ZH\"}";
        String result = HttpUtil.doPost(url, "DeepL-Auth-Key " + apiKey,
                "application/json", json);

        JSONObject jsonObject = JSON.parseObject(result);
        JSONArray translations = jsonObject.getJSONArray("translations");
        return translations.getJSONObject(0).getObject("text", String.class);
    }
}
