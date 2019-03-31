package com.example.shitiku4demo1.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shitiku4demo1.demo.http.HttpUrlTo;
import com.example.shitiku4demo1.demo.http.HttpurlVo;
import com.example.shitiku4demo1.demo.http.Netutil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button requestbt;
    private TextView responsetv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

        //String result= Netutil.sendData("","");
    }

    private void initView() {
        requestbt=findViewById(R.id.send_request);
        responsetv=findViewById(R.id.response_text);
    }

    private void initData() {
        requestbt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.send_request){
            //sendRequestWithHttpURLConnection();//未封装
            //fengzhuang();//httpurlConnection封装
            okhttp();
        }
    }

    private void okhttp() {
        String url="http://192.168.1.105:8080/api/gongjiaojuli";//网络地址
        OkHttpClient okHttpClient=new OkHttpClient();//创建一个okhtp实例
        RequestBody body=new FormBody.Builder()
                .add("UserName","user1")
                .add("zhantai","1")
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: "+e.toString());
                Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse: "+response.toString());
                Toast.makeText(MainActivity.this, "bbb", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fengzhuang() {
        HttpUrlTo httpUrlTo=new HttpUrlTo();
        httpUrlTo
                .setUrl("gongjiaojuli")
                .setJsonobject("UserName","user1")
                .setJsonobject("zhantai","1")
                .setmDialog(this)
                .setLoop(true)
                .setmTime(5000)
                .setHttpVo(new HttpurlVo() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                responsetv.setText(jsonObject.toString());
            }

            @Override
            public void onErrResponse() {

            }
        });
        httpUrlTo.start();
    }

    private void sendRequestWithHttpURLConnection() {
        //开启线程 发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpurl();
            }
        }).start();
    }

    private void httpurl() {
        HttpURLConnection httpURLConnection=null;
        BufferedReader bufferedReader=null;
        try {
            //创建HttpURLConnection实例，需要URL对象，并传入网络地址
            URL url=new URL("http://192.168.1.105:8080/api/gongjiaojuli");
            httpURLConnection= (HttpURLConnection) url.openConnection();
            //设置请求方式
            httpURLConnection.setRequestMethod("POST");
            //设置连接时长
            httpURLConnection.setConnectTimeout(8000);
            httpURLConnection.setReadTimeout(8000);

            //提交服务器JSON数据
            JSONObject obj = new JSONObject();
            obj.put("UserName", "user1");

            //转为字节数值
            byte[] data=(obj.toString()).getBytes();
            // 设置文件长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));



            //获取服务器的输入流
            InputStream in=httpURLConnection.getInputStream();
            //下面对获取到的输入流进行读取
            bufferedReader=new BufferedReader(new InputStreamReader(in));
            StringBuilder response=new StringBuilder();
            String line;//
            while ((line=bufferedReader.readLine())!=null){
                response.append(line);
            }
            showResponse(response.toString());//显示数据
        } catch (Exception e) {
            e.printStackTrace();
        }finally {//关闭缓存流和网络连接
            if (bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
    }

    private void showResponse(final String s1) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responsetv.setText(s1);
            }
        });
    }
}
