package com.ailearningvocabulary.bomnmi.web.controller;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.exception.ProjectException;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.web.service.NLPService;
import com.ailearningvocabulary.bomnmi.web.view.ResponseParagraph;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import com.ailearningvocabulary.bomnmi.web.view.ResponseWordDefinition;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/3 0:43
 */
@RestController
@Api(tags = "handle article generated")
@RequestMapping("/v1/article")
public class ArticleController extends BaseController{


    @ApiOperation(value = "Generate articles", notes = "Connect to gpt and generate articles")
    @GetMapping("/generate-articles")
    public ResponseResult generateArticles(@ApiParam(value = "User id", required = true)
                                           @RequestParam("userId") String userId) {
        //Query wordList
        List<UserVocabulary> userVocabularies = userVocabularyService.queryReviewNeedUserVocabularyByUserIdByNextReviewTime(userId);

        List<Paragraph> paragraphs = null;
        //Generate paragraphs
        try {
            paragraphs = articleService.generateParagraphByVocabularies(userVocabularies, userId);
        } catch (ProjectException projectException) {
            return ResponseResult.error(ResultCode.GENERATE_PARAGRAPH_TIME_OUT);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }


        return ResponseResult.success(paragraphs);
    }

    @ApiOperation(value = "Get paragraphs", notes = "Get the user's unread paragraphs")
    @GetMapping("/get-paragraphs")
    public Object getParagraphs(@ApiParam(value = "User id", required = true)
                                       @RequestParam("userId") String userId) {

        List<Paragraph> paragraphs = null;
        Map<String, List<String>> keyWords = new HashMap<>();
        try {
            paragraphs = articleService.queryUnReadParagraphsByUserId(userId);
            for (Paragraph paragraph : paragraphs) {
                keyWords.put(paragraph.getId(),
                        paragraphVocabularyService.queryWordsByParagraphId(paragraph.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
        //Return Paragraph and wordList.
        ResponseParagraph responseParagraph = new ResponseParagraph();
        responseParagraph.setParagraphs(paragraphs);
        responseParagraph.setWordList(keyWords);

        return responseParagraph;
    }

    @GetMapping("/get-lemmatization-word-list")
    public ResponseResult getLemmatizationWordList(@RequestParam("paragraphId") String paragraphId) {
        List<String> result = null;
        try{
            List<String> wordList = paragraphVocabularyService.queryUnReviewedWordsByParagraphId(paragraphId);
            result = nlpService.getLemmatizationWordList(wordList);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        return ResponseResult.success(result);
    }

    @GetMapping("/get-lemmatization-word")
    public ResponseResult getLemmatizationWord(@RequestParam("word") String word) {
        String result = "";
        try{
            result = nlpService.getLemmatizationWord(word);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        return ResponseResult.success(result);
    }

    @GetMapping("/get-translation-word")
    public ResponseResult getTranslationWord(@RequestParam("word") String word){
        try {
            String result = freeDictionaryService.queryTranslationOrDefinition(word);
            String translation = translationService.queryTranslationOrDefinition(word);
            ResponseWordDefinition responseWordDefinition = new ResponseWordDefinition();
            responseWordDefinition.setDefinition(JSON.parseArray(result));
            responseWordDefinition.setTranslation(translation);
            return ResponseResult.success(responseWordDefinition);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @GetMapping("/get-translation-article")
    public ResponseResult getTranslationArticle(@RequestParam("paragraphId") String paragraphId){
        try {
            Paragraph paragraph = paragraphService.queryParagraphByParagraphId(paragraphId);
            if (paragraph == null) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            String result = paragraph.getTranslationContent();
            return ResponseResult.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }
    @PostMapping("/add-word")
    public ResponseResult addWord(@RequestParam("userId") String userId,
                                  @RequestParam("word") String word) {
        try{
            List<String> words = userVocabularyService.queryAllUserVocabularyByUserId(userId).
                    stream().map(UserVocabulary::getWord).collect(Collectors.toList());

            String lemmatizationWord = nlpService.getLemmatizationWord(word);
            if (words.contains(lemmatizationWord)) {
                return ResponseResult.error(ResultCode.WORD_ALREADY_ADDED);
            }
            userVocabularyService.addWordIntoUserVocabulary(userId, lemmatizationWord);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
        return ResponseResult.success();
    }

    @GetMapping("/get-today-words")
    public ResponseResult getTodayWords(@RequestParam("userId") String userId) {
        List<UserVocabulary> todayWords = null;
        try{
            todayWords = userVocabularyService.queryTodayWordsByUserId(userId);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        return ResponseResult.success(todayWords);
    }

    @PostMapping("/update-next-review-time")
    public ResponseResult updateNextReviewTime(@RequestParam("userId") String userId,
                                               @RequestParam("word") String word,
                                               @RequestParam("quality") Integer quality) {
        try{
            //Query the corresponding vocabulary
            UserVocabulary userVocabulary = userVocabularyService.queryUserVocabularyByPrimaryKey(userId, word);
            if (userVocabulary == null) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            //Set space repetition algorithm to this vocabulary, calculate the next review date
            userVocabulary = superMemo2SpaceRepetitionService.calculateEFAndNextReviewTime(userVocabulary, quality);
            int rows = userVocabularyService.updateUserVocabularyByPrimaryKey(userVocabulary);
            if(rows != 1){
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            return ResponseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @PostMapping("/finish-reading")
    public ResponseResult finishReading(@RequestParam("userId") String userId,
                                        @RequestParam("paragraphId") String paragraphId){
        try {
            paragraphService.updateParagraphStatusAndUserVocabularyStatus(userId, paragraphId);
            return ResponseResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }
}
