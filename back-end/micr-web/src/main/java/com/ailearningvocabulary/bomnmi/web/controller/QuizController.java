package com.ailearningvocabulary.bomnmi.web.controller;

import com.ailearningvocabulary.bomnmi.api.model.Quiz;
import com.ailearningvocabulary.bomnmi.api.pojo.QuizSetting;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.web.view.ResponseQuizProblem;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/18 20:31
 */
@RestController
@Api(tags = "handle quiz setting and problem")
@RequestMapping("/v1/quiz")
public class QuizController extends BaseController{

    @PostMapping("/set-quiz")
    public ResponseResult setQuiz(@RequestParam("userId") String userId,
                                  @RequestBody QuizSetting quizSetting) {
        List<ResponseQuizProblem> responseQuizProblems = quizPageService.generateQuizProblem(userId, quizSetting);

        return ResponseResult.success(responseQuizProblems);
    }

    @PostMapping("/add-quiz")
    public ResponseResult addQuiz(@RequestParam("userId") String userId,
                                  @RequestParam("score") Double score){
        try{
            int count = quizService.insertQuizByUserIdAndScore(userId, score);
            if (count != 1) {
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
            return ResponseResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @GetMapping("/get-history-quiz")
    public ResponseResult getHistoryQuiz(@RequestParam("userId") String userId) {
        try{
            List<Quiz> quizzes = quizService.queryAllQuizByUserId(userId);
            return ResponseResult.success(quizzes);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

    }
}
