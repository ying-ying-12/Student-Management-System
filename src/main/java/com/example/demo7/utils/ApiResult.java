package com.example.demo7.utils;
//工具类
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

//用于给前端ajax请求返回信息
public class ApiResult {

    private Boolean success;//判断是否删除成功或者登录是否成功
    private String message;//提示信息
    private Object data;//返回数据给前端

    public static String json(Boolean success,String message,Object data){
        ApiResult r = new ApiResult();
        r.setData(data);
        r.setMessage(message);
        r.setSuccess(success);
        String json = JSON.toJSONString(r, 
            SerializerFeature.DisableCircularReferenceDetect,
            SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullListAsEmpty);
        return json;
    }

    //重载
    // Getter和Setter方法，用于封装数据
    public static String json(Boolean success,String message){
        return json(success,message,null);
    }


    public Boolean getSuccess() {
        return success; // 返回操作状态
    }

    public void setSuccess(Boolean success) {
        this.success = success; // 设置操作状态
    }

    public String getMessage() {
        return message; // 返回提示消息
    }

    public void setMessage(String message) {
        this.message = message; // 设置提示消息
    }

    public Object getData() {
        return data; // 返回响应数据
    }

    public void setData(Object data) {
        this.data = data; // 设置响应数据
    }
}