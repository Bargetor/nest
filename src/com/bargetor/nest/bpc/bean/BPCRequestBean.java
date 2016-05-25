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
    private BPCRequestMetaBean meta;
    private String params;
    @ParamCheck(isRequired = true)
    private String method;
    /**
     * API version
     */
    private String api;

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

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public BPCRequestMetaBean getMeta() {
        return meta;
    }

    public void setMeta(BPCRequestMetaBean meta) {
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "BPCRequestBean{" +
                "id='" + id + '\'' +
                ", bpc='" + bpc + '\'' +
                ", meta=" + meta +
                ", params='" + params + '\'' +
                ", method='" + method + '\'' +
                ", api='" + api + '\'' +
                '}';
    }
}
