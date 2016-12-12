package com.bargetor.nest.common.check.param;

import com.bargetor.nest.bpc.exception.BPCException;

/**
 * Created by bargetor on 2016/12/12.
 */
public class ParamCheckError extends BPCException {
    public ParamCheckError(String msg){
        super(msg);
    }
}
