package com.bargetor.nest.bpc.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCResponseBean;
import com.bargetor.nest.bpc.servlet.BPCRequest;
import com.bargetor.nest.bpc.servlet.BPCResponse;
import com.bargetor.nest.common.bpc.BPCUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCReturnValueHandler {

    public void process(BPCRequest request, BPCResponse response, Object returnValue){
        BPCResponseBean responseBean = response.getResponseBean();
        responseBean.setResult(returnValue);
        BPCUtil.writeResponse(
                response.getHttpResponse(),
                JSON.toJSONString(responseBean, SerializerFeature.DisableCircularReferenceDetect)
        );
    }
}
