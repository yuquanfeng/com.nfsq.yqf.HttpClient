package com.nfsq.yqf.testhttpclient.http;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

@Component
public class HttpConnectManager {
    PoolingHttpClientConnectionManager cm = null;
    @PostConstruct
    public void init(){
        LayeredConnectionSocketFactory sslsf = null;
        try{
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        }catch (Exception e){
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(100);
        cm.setDefaultMaxPerRoute(20);
    }

    public CloseableHttpClient getHttpClient(){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
    }
}
