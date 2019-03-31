package com.example.shitiku4demo1.demo.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;


/**
 * 代码创建时间 2019/3/21
 * 创建人 库金
 * 无敌是多么寂寞！！！！！
 */
public class HttpUrlTo extends Thread {
    private String mUrl="http://192.168.1.105:8080/api/";
    private JSONObject mJsonObject=new  JSONObject();
    private int mTime=0;
    private boolean misLoop=false;
    private ProgressDialog mDialog;
    private HttpurlVo mhttpurlVo;
    private Handler mHandler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==0x001){
                String result= (String) msg.obj;
                if (result.equals("")){
                    mhttpurlVo.onErrResponse();
                    dimssDialog();
                }else {
                    try {
                        mhttpurlVo.onResponse(new JSONObject(result));
                        dimssDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mhttpurlVo.onErrResponse();
                        dimssDialog();
                    }
                }

            }
            return false;
        }
    });

    private void dimssDialog() {
        if (mDialog!=null&&mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    public HttpUrlTo setUrl(String url){
        mUrl+=url;
        return this;
    }

    public HttpUrlTo setJsonobject(String k,Object v){
        try {
            mJsonObject.put(k,v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public HttpUrlTo setmTime(int time){
        mTime=time;
        return this;
    }

    public HttpUrlTo setLoop(boolean loop){
        misLoop=loop;
        return this;
    }

    public HttpUrlTo setmDialog(Context context){
        mDialog=new ProgressDialog(context);
        mDialog.setTitle("提示");
        mDialog.setMessage("网络请求中");
        mDialog.show();
        return this;
    }

    public HttpUrlTo setHttpVo(HttpurlVo httpVo){
        mhttpurlVo=httpVo;
        return this;
    }

    @Override
    public void run() {
        super.run();
        do {
            Netutil netutil=new Netutil();
            String result=netutil.sendData(mUrl,mJsonObject.toString());
            Message message=new Message();
            message.what=0x001;
            message.obj=result;
            mHandler.sendMessage(message);
            try {
                Thread.sleep(mTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (misLoop);
    }
}
