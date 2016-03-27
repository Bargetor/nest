package com.bargetor.nest.bpc.bean;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCResponseBean {
    private String id;
    private String bpc;
    private Object result;
    private Object error;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
