package com.ailearningvocabulary.bomnmi.common.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author Bomnmi
 * @version 1.0
 * @date 2023/8/8 16:17
 */
public class HttpUtil {

    public static String doGet(String url) throws IOException {
        //GET request
        //Create HttpClient object
        CloseableHttpClient client = HttpClients.createDefault();

        //Create Http's get object
        HttpGet httpGet = new HttpGet(url);

        //Execute request
        CloseableHttpResponse response = client.execute(httpGet);
        String json = "";
        //get information from response
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //get data
            json = EntityUtils.toString(response.getEntity());
        }
        //close resource
        client.close();
        return json;
    }

    public static String doPost(String url, String authorization, String contentType, String json) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        if (authorization != null) {
            httpPost.setHeader("Authorization", authorization);
        }
        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        }
        StringEntity requestEntity = new StringEntity(json);
        httpPost.setEntity(requestEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        String resultJson = "";
        if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            //get data
            resultJson = EntityUtils.toString(response.getEntity());
        }
        client.close();
        return resultJson;
    }
}
