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

    private BPCReturnValueHandler returnValueHandler;
    private BPCExceptionHandler exceptionHandler;

    public void process(BPCRequest request, BPCResponse response){
        try {
            Object returnValue = this.invokeMethod(request.getMethod(), request.getRequestBean());
            this.returnValueHandler.process(request.getHttpRequest(), response.getHttpResponse(), request.getRequestBean(), returnValue);
        }catch (Throwable e){
            logger.error("process error", e);
            this.exceptionHandler.process(request.getHttpRequest(), response.getHttpResponse(), request.getRequestBean(), e);
        }
    }

    private Object invokeMethod(BPCServiceMethod method, BPCRequestBean requestBean) throws Throwable {
        logger.info(String.format("%s -> bpc invoke method {%s}", requestBean.getId(), requestBean.getMethod()));
        if(method == null)return null;
        return method.invoke(requestBean);
    }


    public BPCReturnValueHandler getReturnValueHandler() {
        return returnValueHandler;
    }

    public void setReturnValueHandler(BPCReturnValueHandler returnValueHandler) {
        this.returnValueHandler = returnValueHandler;
    }

    public BPCExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(BPCExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }
}
