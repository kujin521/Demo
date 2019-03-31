package com.example.shitiku4demo1.demo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 代码创建时间 2019/3/21
 * 创建人 库金
 * 无敌是多么寂寞！！！！！
 */
public class Netutil {
    private static URL urlstr;
    private static HttpURLConnection connection;
    private static BufferedReader reader;

    public static String sendData(String urlString,String params){
        String result="";
        createrConnection(urlString);
        setParams();
        writeData(params);
        result=readData(result);
        return result;
    }

    private static void writeData(String params) {
        try {
            OutputStream os=connection.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
            osw.write(params);
            osw.flush();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readData(String result) {
        try {
            reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            if ((line=reader.readLine())!=null){
                result+=line;
            }else {
                result+="\n"+line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void setParams() {
        connection.setReadTimeout(20*1000);
        connection.setConnectTimeout(20*1000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private static void createrConnection(String urlString) {
        try {
            urlstr= new URL(urlString);
            connection= (HttpURLConnection) urlstr.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
