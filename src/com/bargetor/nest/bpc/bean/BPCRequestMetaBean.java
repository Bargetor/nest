package com.bargetor.nest.bpc.bean;

/**
 * Created by Bargetor on 16/5/25.
 */
public class BPCRequestMetaBean {
    private int userId;
    private String token;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", token='" + token + '\'' +
                '}';
    }
}
