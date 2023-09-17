package com.ailearningvocabulary.bomnmi.web.service.impl;

import com.ailearningvocabulary.bomnmi.api.model.Vocabulary;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.common.utils.HttpUtil;
import com.ailearningvocabulary.bomnmi.web.service.TranslationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/10 16:02
 */
@Service("freeDictionaryService")
public class FreeDictionaryDefinitionServiceImpl implements TranslationService {
    @Value("${ai.free-dicitionay.url}")
    private String url;
    @Override
    public String queryTranslationOrDefinition(String word) throws IOException {
        return HttpUtil.doGet(url + word);
    }
}
