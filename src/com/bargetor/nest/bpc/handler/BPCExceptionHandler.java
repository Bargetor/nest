package com.bargetor.nest.bpc.handler;

import com.alibaba.fastjson.JSON;
import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCResponseBean;
import com.bargetor.nest.bpc.exception.BPCException;
import com.bargetor.nest.bpc.servlet.BPCRequest;
import com.bargetor.nest.bpc.servlet.BPCResponse;
import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCExceptionHandler {
    public void process(BPCRequest request, BPCResponse response, Throwable e){
        BPCResponseBean responseBean = response.getResponseBean();
        if(BPCException.class.isAssignableFrom(e.getClass())){
            JSON errorJson = ((BPCException)e).toJson();
            responseBean.setError(errorJson);
        }else {
            responseBean.setError(e);
        }
        String responseJsonString = JSON.toJSONString(responseBean);
        BPCUtil.writeResponse(response.getHttpResponse(), responseJsonString);
    }
}
