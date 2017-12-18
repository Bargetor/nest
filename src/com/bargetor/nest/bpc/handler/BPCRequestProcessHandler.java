package com.bargetor.nest.bpc.handler;

import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCServiceMethod;
import com.bargetor.nest.bpc.servlet.BPCRequest;
import com.bargetor.nest.bpc.servlet.BPCResponse;
import org.apache.log4j.Logger;

/**
 * Created by Bargetor on 16/3/20.
 */
public final class BPCRequestProcessHandler {
    private static final Logger logger = Logger.getLogger(BPCRequestProcessHandler.class);

    public Object process(BPCRequest request, BPCResponse response) throws Throwable {
        return this.invokeMethod(request.getMethod(), request.getRequestBean());
    }

    private Object invokeMethod(BPCServiceMethod method, BPCRequestBean requestBean) throws Throwable {
        logger.info(String.format("%s -> bpc invoke method {%s}, params: %s", requestBean.getId(), requestBean.getMethod(), requestBean.getParams()));
        if(method == null)return null;
        return method.invoke(requestBean);
    }
}
