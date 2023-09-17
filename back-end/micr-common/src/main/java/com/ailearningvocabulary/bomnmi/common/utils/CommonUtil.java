package com.ailearningvocabulary.bomnmi.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/7/29 14:00
 */
public class CommonUtil {
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final String JWT_KEY = "3f53a245182d4624bdbb9350d46aa46a";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String YOU_DAO_PRONUNCIATION_URL = "https://dict.youdao.com/dictvoice?";

    public static boolean verifyEmailFormat(String email) {
        return pattern.matcher(email).matches();
    }

    public static String[] youDaoPronunciationUrl(String word) {
        String us = YOU_DAO_PRONUNCIATION_URL + "type=0&audio=" + word;
        String uk = YOU_DAO_PRONUNCIATION_URL + "type=1&audio=" + word;
        return new String[]{us, uk};
    }

    public static boolean verifyPasswordFormat(String password) {

        return password.length() == 32;
    }

    public static String encryptSaltedPasswordUsingMd5(String SaltedPassword) {
        return DigestUtils.md5Hex(SaltedPassword);
    }

    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getNDigitsRandomNumber(int n){
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder("");
        //Generate a 4 random digits code
        for (int i = 0; i < n; i++) {
            verificationCode.append(random.nextInt(10));
        }
        return verificationCode.toString();
    }

    public static int getRandomNumberLessThenN(int n){
        Random random = new Random();
        return random.nextInt(n);
    }

    /**
     * Prase the LocalDateTime object to String format: xxxx-xx-xx
     * @return
     */
    public static String parseLocalDateTimeToDate(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return dateTime.format(formatter);
    }

    /**
     *
     * @param t A POJO that want to use to generate JWT
     * @param expireTime The expiration time, unit is minutes
     * @param <T> generic class
     * @return return jwt string
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static <T> String generateJwt(T t, Integer expireTime) throws InvocationTargetException, IllegalAccessException {
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
        Date currentDate = new Date();
        String jwt = Jwts.builder().signWith(secretKey, SignatureAlgorithm.HS256)
                .setExpiration(DateUtils.addMinutes(currentDate, expireTime))
                .setIssuedAt(currentDate)
                .setId(generateUUID().toUpperCase())
                .addClaims(ReflectUtil.parseObjectToMap(t))
                .compact();

        return jwt;
    }

    public static Claims readJwt(String jwt){
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
        Claims body = Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(jwt).getBody();
        return body;
    }

    /**
     * This method is used to slice an array, let it be separated as several groups, store in a list.
     *
     * if the length of array is not enough long to build a new group with length 'number',
     * then if: the residual length bigger than half of 'number', let it be a new group,
     *      else: let the residual elements merge into the previous groups.
     * for example:
     *      arr:[1,2,3,4,5,6,7,8,9]       number:6
     *      return: [[1, 2, 3, 4, 5, 6], [7, 8, 9]]
     *
     *      arr:[1,2,3,4,5,6,7,8]       number:6
     *      return: [[1, 2, 3, 4, 5, 6, 7, 8]]
     *
     * @param arr the array waiting to be sliced
     * @param number how many elements in one group.
     * @return The seperated of array.
     */
    public static List<List<String>> sliceArray(List<String> arr, int number){
        List<List<String>> requestContent = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (int i = 1; i < arr.size() + 1; i++) {
            tempList.add(arr.get(i - 1));
            if (i % number == 0 || i == arr.size()) {
                if (arr.size() - i >= number / 2) {
                    requestContent.add(tempList);
                    tempList = new ArrayList<>();
                } else if (arr.size() == i) {
                    requestContent.add(tempList);
                }
            }
            if (requestContent.size() == 3) {
                break;
            }
        }
        return requestContent;
    }

    public static LocalDateTime handleNullLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime == null ? LocalDateTime.of(2001, 7, 14, 0, 0, 0) : localDateTime;
    }

}
