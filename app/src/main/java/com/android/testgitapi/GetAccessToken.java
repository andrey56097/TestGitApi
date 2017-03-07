package com.android.testgitapi;

/**
 * Created by batsa on 28.02.2017.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

public class GetAccessToken {



    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public GetAccessToken() {
    }

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    DefaultHttpClient httpClient;
    HttpPost httpPost;
    OkHttpClient okHttpClient;

    ContentValues values=new ContentValues();



    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String [] sp = param.split("=");
            String name = param.split("=")[0];
            String value = "";
            if(sp.length > 1)
                value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    public JSONObject gettoken(String address, String token, String client_id, String client_secret, String redirect_uri, String grant_type)  {
        // Making HTTP request
        try {
            // DefaultHttpClient
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

//            okHttpClient = new OkHttpClient();


            params.add(new BasicNameValuePair("code", token));
            params.add(new BasicNameValuePair("client_id", client_id));
            params.add(new BasicNameValuePair("client_secret", client_secret));
            params.add(new BasicNameValuePair("redirect_uri", redirect_uri));
            params.add(new BasicNameValuePair("grant_type", "grant_type"));

            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            //httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("User-Agent", "Awesome-Octocat-App");


            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> map = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 1024);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();


            json = sb.toString();
            Log.e("JSONStr", json);
            map = getQueryMap(json);
        } catch (Exception e) {
            e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Parse the String to a JSON Object
        jObj = new JSONObject(map);
        // Return JSON String
        return jObj;
    }

}
