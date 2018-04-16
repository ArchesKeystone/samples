package com.arches.keystone.samples.util;

import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import sun.misc.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for REST operations
 */
public class RestClient {

    String userName;
    String password;

    String keystoneUrl;
    String apiKey;

    Gson gson = new Gson();

    public static final Integer GET = 1;
    public static final Integer POST = 2;
    public static final Integer PUT = 3;
    public static final Integer DELETE = 4;

    public RestClient(String url, String userName, String password){
        this.keystoneUrl = url;
        this.userName = userName;
        this.password = password;
    }


    public <T extends Serializable> T sendRequest(String url, Method method, Object request, Class<T> responseType) throws Exception {

        CloseableHttpClient client = HttpClients.createDefault();
        String requestUrl = keystoneUrl + url;
        HttpRequestBase httpReq = null;
        UsernamePasswordCredentials creds
                = new UsernamePasswordCredentials(userName, password);
        StringEntity entity = null;
        if(request != null) {

            String payload = gson.toJson(request);
            entity = new StringEntity(payload);
        }
        if (method == Method.GET) {
            httpReq = new HttpGet(requestUrl);
        } else if (method == Method.POST) {
            httpReq = new HttpPost(requestUrl);
            if(entity != null)
                ((HttpPost) httpReq).setEntity(entity);
        } else if (method == Method.PUT) {
            httpReq = new HttpPut(requestUrl);
            if(entity != null)
                ((HttpPut) httpReq).setEntity(entity);
        } else if (method == Method.DELETE) {
            httpReq = new HttpDelete(requestUrl);
        }
        httpReq.addHeader(new BasicScheme().authenticate(creds, httpReq, null));
        httpReq.setHeader("Accept", "application/json");
        httpReq.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(httpReq);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = response.getEntity().getContent().read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        byte[] byteArray = buffer.toByteArray();

        String out = new String(byteArray, StandardCharsets.UTF_8);
        System.out.println(out);

        client.close();
        T resp = (T) gson.fromJson(out, responseType);
        return resp;
    }

    public <T extends Serializable> T get(String url, Class<T> responseType) throws Exception {
        return sendRequest(url, Method.GET, null, responseType);
    }

    public <T extends Serializable> T post(String url, Object request, Class<T> responseType) throws Exception {
        return sendRequest(url, Method.POST, request, responseType);
    }

    public <T extends Serializable> T put(String url, Object request, Class<T> responseType) throws Exception {
        return sendRequest(url, Method.PUT, request, responseType);
    }

    public <T extends Serializable> T delete(String url, Class<T> responseType) throws Exception {
        return sendRequest(url, Method.DELETE, null, responseType);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeystoneUrl() {
        return keystoneUrl;
    }

    public void setKeystoneUrl(String keystoneUrl) {
        this.keystoneUrl = keystoneUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }


}

enum Method {
    GET, POST, PUT, DELETE
}
