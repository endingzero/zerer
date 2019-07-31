package com.zjw.zerer.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Result<T> {

    public final static Integer CODE_SUCCESS = 0;
    public final static Integer CODE_FAIL = 1;
    public final static String MSG_SUCCESS = "操作成功";
    public final static String MSG_FAIL = "操作失败";

   private static ObjectMapper mapper=  new ObjectMapper();

    private Integer code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private T data;

    public static <T> Result<T> build(Integer status, String msg, T data) {
        return new Result<T>(status, msg, data);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(data);
    }

    public static <T> Result<T> ok() {
        return new Result<T>(CODE_SUCCESS, MSG_SUCCESS, (T)mapper.createObjectNode());
    }

    public static <T> Result<T> fail() {
        return new Result<T>(CODE_FAIL, MSG_FAIL, null);
    }

    public static <T> Result<T> fail(EnumCode enumCode) {
      
        return new Result<T>(enumCode.getCode(), enumCode.getMsg(), (T)mapper.createObjectNode());
    }

    public Result() {

    }

    public static <T> Result<T> build(Integer status, String msg) {
        return new Result<T>(status, msg, (T)mapper.createObjectNode());
    }

    public static <T> Result<T> getResult(T t) {
        Result<T> result = new Result<T>(t);
        return result;
    }

    public Result(Integer status, String msg, T data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    public Result(T data) {
        this.code = 0;
        this.msg = MSG_SUCCESS;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public static String toJson(Result<?> result) {
      ObjectMapper mapper = new ObjectMapper();
      try {
        return mapper.writeValueAsString(result);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      return "";
    }

}
