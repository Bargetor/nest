package com.bargetor.nest.bpc.servlet;

import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCServiceMethod;
import com.bargetor.nest.bpc.manager.BPCDispatchManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCRequest {
    private HttpServletRequest httpRequest;
    private BPCRequestBean requestBean;
    private BPCServiceMethod method;

    public BPCRequest( HttpServletRequest httpRequest, BPCRequestBean requestBean){
        this.httpRequest = httpRequest;
        this.requestBean = requestBean;

        this.findMethod();
    }

    private void findMethod(){
        String url = this.httpRequest.getRequestURI().toString();
        this.method = BPCDispatchManager.getInstance().getServiceMethod(url, requestBean.getMethod());
    }


    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    public void setHttpRequest(HttpServletRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public BPCRequestBean getRequestBean() {
        return requestBean;
    }

    public void setRequestBean(BPCRequestBean requestBean) {
        this.requestBean = requestBean;
    }

    public BPCServiceMethod getMethod() {
        return method;
    }

    public void setMethod(BPCServiceMethod method) {
        this.method = method;
    }
}
