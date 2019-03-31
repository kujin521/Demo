package com.example.shitiku4demo1.demo.http;

import org.json.JSONObject;

/**
 * 代码创建时间 2019/3/21
 * 创建人 库金
 * 无敌是多么寂寞！！！！！
 */
public interface HttpurlVo {
    void onResponse(JSONObject jsonObject);
    void onErrResponse();
}
