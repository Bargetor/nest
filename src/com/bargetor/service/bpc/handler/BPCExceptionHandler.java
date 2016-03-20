package com.bargetor.service.bpc.handler;

import com.alibaba.fastjson.JSON;
import com.bargetor.service.bpc.bean.BPCRequestBean;
import com.bargetor.service.bpc.bean.BPCResponseBean;
import com.bargetor.service.common.bpc.BPCUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCExceptionHandler {
    public void process(HttpServletRequest req, HttpServletResponse resp, BPCRequestBean requestBean, Throwable e){
        resp.setContentType("application/json;charset=utf-8");
        BPCResponseBean responseBean = new BPCResponseBean();
        responseBean.setId(requestBean.getId());
        responseBean.setBpc(requestBean.getBpc());
        responseBean.setError(e);
        BPCUtil.writeResponse(resp, JSON.toJSONString(responseBean));
    }
}
