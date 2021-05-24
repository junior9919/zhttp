package com.halo;

import java.util.List;

public class ResponseCodeTableBean {
    private String code;

    private String message;

    private List<CodeTableBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CodeTableBean> getData() {
        return data;
    }

    public void setData(List<CodeTableBean> data) {
        this.data = data;
    }
}
