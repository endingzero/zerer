package com.zjw.core.util;

public enum EnumCode {

    // 全局错误
    FAIL(Result.CODE_FAIL, Result.MSG_FAIL),
    TOKEN_ERR(-1,"token无效"),
    SIGN_ERR(-2,"签名sign无效"),
    PARAM_NULL(-3,"参数不能为空"),
    MOBILE_ERROR(-4,"手机号必须是11位数"),
    FILE_NULL(7,"文件不能为空"),
    FILE_APK(80001,"文件类型必须为apk"),
    BAD_REQUEST(400,"参数有误"),
    NOT_FOUND(404,"未查找到数据"),
    NET_ERROR(404,"网络异常"),
    ACCOUNT_ERROR(556,"account-service 服务异常"),
    SERVICE_ERROR(500,"服务异常"),
    ;
    
    private int code;
    private String msg;

    EnumCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    
    public int getCode() {
        return code;
    }

    public String getCodeStr() {
        return code + "";
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public EnumCode setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public static String getMsgByCode(int code) {
        EnumCode[] values = EnumCode.values();
        for (EnumCode ec : values) {
            if (ec.code == code) {
                return ec.msg;
            }
        }
        return "";
    }
}
