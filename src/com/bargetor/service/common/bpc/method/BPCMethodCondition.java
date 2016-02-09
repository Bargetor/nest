package com.bargetor.service.common.bpc.method;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Bargetor on 16/1/28.
 */
public class BPCMethodCondition implements RequestCondition<BPCMethodCondition> {
    private String method;

    public BPCMethodCondition(BPCMethod method){
        this.method = method.value();
    }

    public BPCMethodCondition(String method){
        this.method = method;
    }

    @Override
    public BPCMethodCondition combine(BPCMethodCondition other) {
        return new BPCMethodCondition(other.getMethod());
    }

    @Override
    public BPCMethodCondition getMatchingCondition(HttpServletRequest request) {
        return null;
    }

    @Override
    public int compareTo(BPCMethodCondition other, HttpServletRequest request) {
        return other.getMethod().compareTo(this.getMethod());
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
