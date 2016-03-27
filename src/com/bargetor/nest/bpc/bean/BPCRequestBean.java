package com.bargetor.nest.bpc.bean;

import com.bargetor.nest.common.check.param.ParamCheck;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCRequestBean {
    @ParamCheck(isRequired = true)
    private String id;
    @ParamCheck(isRequired = true)
    private String bpc;
    private String username;
    private String params;
    @ParamCheck(isRequired = true)
    private String method;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBpc() {
        return bpc;
    }

    public void setBpc(String bpc) {
        this.bpc = bpc;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "BPCRequestBean{" +
                "id='" + id + '\'' +
                ", bpc='" + bpc + '\'' +
                ", username='" + username + '\'' +
                ", params='" + params + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}
