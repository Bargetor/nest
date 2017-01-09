package com.bargetor.nest.bpc.exception;

import com.bargetor.nest.common.bpc.bean.BPCResponseError;

/**
 * Created by Bargetor on 16/4/13.
 */
public class BPCMethodParameterValueCheckFailError extends BPCException {

    public BPCMethodParameterValueCheckFailError(String paramName){
        super(paramName + " method parameter value check fail");
    }
}
