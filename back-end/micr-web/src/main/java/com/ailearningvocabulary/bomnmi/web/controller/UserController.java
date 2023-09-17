package com.ailearningvocabulary.bomnmi.web.controller;

import com.ailearningvocabulary.bomnmi.api.model.Paragraph;
import com.ailearningvocabulary.bomnmi.api.model.Record;
import com.ailearningvocabulary.bomnmi.api.model.User;
import com.ailearningvocabulary.bomnmi.api.model.UserVocabulary;
import com.ailearningvocabulary.bomnmi.common.enums.ResultCode;
import com.ailearningvocabulary.bomnmi.common.utils.CommonUtil;
import com.ailearningvocabulary.bomnmi.common.utils.ReflectUtil;
import com.ailearningvocabulary.bomnmi.web.view.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/27 22:07
 */
@RestController
@Api(tags = "handle user login and register")
@RequestMapping("/v1/user")
public class UserController extends BaseController {

    @PostMapping("/login")
    @ApiOperation(value = "User Login", notes = "Check email and password")
    @ApiResponses({
            @ApiResponse(code = 3003, message = "user is not exist"),
            @ApiResponse(code = 3004, message = "email or password is wrong"),
            @ApiResponse(code = 2000, message = "system inner error"),
    })
    public ResponseResult userLogin(@ApiParam(value = "User email", required = true)
                                    @RequestParam("loginEmail") String email,
                                    @ApiParam(value = "User password", required = true)
                                    @RequestParam("loginPassword") String password) {

        //Check email has been registered
        if (!userService.checkEmailIsRegistered(email)) {
            return ResponseResult.error(ResultCode.USER_NOT_EXIST);
        }

        //Check the password and email is correct
        User user = userService.queryUserEmailAndPasswordAreCorrect(email, password);
        if (user == null) {
            //Password or email is incorrect
            return ResponseResult.error(ResultCode.WRONG_EMAIL_OR_PASSWORD);
        }
        int rows = userService.updateUserLoginTime(user.getId());
        if (rows != 1) {
            //System error, can not update last login time
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR.getResultCode(), "Can not update last login time");
        }

        //Add record
        Record record = recordService.queryRecordByUserIdAndTime(user.getId(), LocalDateTime.now());
        if (record == null) {
            record = new Record();
            record.setId(CommonUtil.generateUUID());
            record.setUserId(user.getId());
            record.setLoginTime(LocalDateTime.now());
            rows = recordService.addRecord(record);
            if(rows != 1){
                return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
            }
        }

        //Set token
        User jwtUser = new User();
        jwtUser.setEmail(user.getEmail());
        jwtUser.setId(user.getId());
        jwtUser.setUserName(user.getUserName());

        try {
            String jwt = CommonUtil.generateJwt(jwtUser, 120);
            //Successfully log in, pass user to front-end
            ResponseResult responseResult = ResponseResult.success(ReflectUtil.parseObjectToMap(jwtUser));
            responseResult.setAccessToken(jwt);
            return responseResult;
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @PostMapping("/email-check")
    @ApiOperation(value = "Check Email", notes = "Check a email is registered")
    @ApiResponses({
            @ApiResponse(code = 3005, message = "this email has already been registered"),
    })
    public ResponseResult checkEmail(@ApiParam(value = "Register email", required = true)
                                     @RequestParam("registerEmail") String email) {

        //Check email has not been registered
        if (userService.checkEmailIsRegistered(email)) {
            return ResponseResult.error(ResultCode.USER_REGISTERED);
        }

        return ResponseResult.success();
    }

    @PostMapping("/send-verification-code")
    @ApiOperation(value = "Send Verification Code",
            notes = "Send a 4 digit verification code to user's email")
    @ApiResponses({
            @ApiResponse(code = 3005, message = "this email has already been registered"),
            @ApiResponse(code = 2000, message = "system inner error"),
    })
    public ResponseResult sendVerificationCode(@ApiParam(value = "Register email", required = true)
                                               @RequestParam("registerEmail") String email) {

        //Check email has not been registered
        if (userService.checkEmailIsRegistered(email)) {
            return ResponseResult.error(ResultCode.USER_REGISTERED);
        }
        try {
            //send verification code to user
            emailService.sendVerificationCode(email);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        return ResponseResult.success();
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register User",
            notes = "Register a new user into database")
    @ApiResponses({
            @ApiResponse(code = 3006, message = "the verification code is wrong"),
            @ApiResponse(code = 2000, message = "system inner error"),
    })
    public ResponseResult userRegister(@ApiParam(value = "Register email", required = true)
                                       @RequestParam("registerEmail") String email,
                                       @ApiParam(value = "Register password", required = true)
                                       @RequestParam("registerPassword") String password,
                                       @ApiParam(value = "Verification code", required = true)
                                       @RequestParam("verificationCode") String verificationCode) {

        //Check the user's verification code
        if (!emailService.checkVerificationCode(email, verificationCode)) {
            return ResponseResult.error(ResultCode.VERIFICATION_CODE_WRONG);
        }
        User user = userService.queryUserEmailAndPasswordAreCorrect(email, password);
        if (user != null) {
            user.setPassword("");
            user.setCreateTime(null);
            user.setLastLoginTime(null);
            return tokenService.setTokenIntoResponseResult(user);
        }
        int rows = userService.registerNewUser(email, password);
        if (rows != 1) {
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        //Get new user and sent it to front-end, exclude password
        user = userService.queryUserEmailAndPasswordAreCorrect(email, password);
        user.setPassword("");
        user.setCreateTime(null);
        user.setLastLoginTime(null);
        return tokenService.setTokenIntoResponseResult(user);
    }

    @GetMapping("/check-status")
    public ResponseResult checkUserStatus(@RequestParam("userId") String userId) {
        //Check user is new(There is not any words added by this user)
        List<UserVocabulary> userVocabularies = userVocabularyService.queryReviewNeedUserVocabularyByUserIdByNextReviewTime(userId);
        if (userVocabularies.size() == 0) {
            //User finish review or user is new to system
            return new ResponseResult(ResultCode.WAIT_LEARN_USER);
        }
        //Means all words have been already reviewed today.
        return new ResponseResult(ResultCode.WAIT_REVIEW_USER);
    }

    @GetMapping("/get-user-last-login-time")
    public ResponseResult getUserLastLoginTime(@RequestParam("userId") String userId) {
        //Get the user's last login time
        try{
            User user = userService.queryUserById(userId);
            String timeString = CommonUtil.parseLocalDateTimeToDate(user.getLastLoginTime());
            return ResponseResult.success(timeString);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }
    }

    @PostMapping("/update-username")
    public ResponseResult updateUserName(@RequestParam("userId") String userId,
                                         @RequestParam("newUserName") String userName){
        User user = new User();
        user.setUserName(userName);
        user.setId(userId);
        int rows = userService.updateSelectiveUser(user);
        if (rows != 1) {
            return ResponseResult.error(ResultCode.SYSTEM_INNER_ERROR);
        }

        return ResponseResult.success();
    }




}
