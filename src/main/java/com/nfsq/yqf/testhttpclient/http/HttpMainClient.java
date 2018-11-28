package com.nfsq.yqf.testhttpclient.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class HttpMainClient {
    @Autowired
    HttpConnectManager manager;

    /**
     * 此方法用来发起http请求
     * @param request
     * @return
     */
    public String getJson(HttpUriRequest request){
        String json = null;
        CloseableHttpClient httpClient = manager.getHttpClient();
        HttpResponse response = null;
        try {
            response = httpClient.execute(request);
            InputStream in = response.getEntity().getContent();
            json = IOUtils.toString(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * jsonPost方式
     * @param url
     * @param param
     * @return
     */
    public String postJson(String url, String param){
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(param, ContentType.APPLICATION_JSON);// 构造请求数据
        httpPost.setEntity(entity);// 设置请求体
        return getJson(httpPost);
    }

    /**
     * 此方法用来转换参数成想要的格式然后调用getJson方法发起http请求(GET请求)
     * @param url
     * @param params
     * @return
     */
    public String get(String url, Map<String,Object> params){
        StringBuilder sb = new StringBuilder();
        if(params != null && params.size()>0){
            for(Map.Entry<String,Object> entry : params.entrySet()){
                if(entry.getValue() != null){
                    sb.append("&"+entry.getKey()+"="+entry.getValue());
                }
            }
            if(sb.length()>1){
                url+="?"+sb.substring(1);
            }
        }
        HttpGet httpGet = new HttpGet(url);
        return getJson(httpGet);
    }

    /**
     * 此方法用来发起post请求
     * @param url
     * @param params
     * @return
     */
    public  String post(String url,Map<String,Object> params){
        HttpPost httpPost = new HttpPost(url);
        if(params != null && params.size()>0){
            List<NameValuePair> formParams = new ArrayList<>();
            for(Map.Entry<String,Object> entry : params.entrySet()){
                formParams.add(new BasicNameValuePair(entry.getKey(),String.valueOf(entry.getValue())));
            }
            try {
                HttpEntity httpEntity = new UrlEncodedFormEntity(formParams,"utf-8");
                httpPost.setEntity(httpEntity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return getJson(httpPost);
    }



}
