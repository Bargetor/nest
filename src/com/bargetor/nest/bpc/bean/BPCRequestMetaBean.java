package com.bargetor.nest.bpc.bean;

/**
 * Created by Bargetor on 16/5/25.
 */
public class BPCRequestMetaBean {
    private int userid;
    private String token;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
