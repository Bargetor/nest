package com.bargetor.nest.bpc.servlet;

import com.bargetor.nest.bpc.bean.BPCResponseBean;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCResponse {
    private HttpServletResponse httpResponse;
    private BPCResponseBean responseBean;

    public BPCResponse(HttpServletResponse httpResponse, BPCResponseBean responseBean){
        this.httpResponse = httpResponse;
        this.responseBean = responseBean;
    }

    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(HttpServletResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public BPCResponseBean getResponseBean() {
        return responseBean;
    }

    public void setResponseBean(BPCResponseBean responseBean) {
        this.responseBean = responseBean;
    }
}
