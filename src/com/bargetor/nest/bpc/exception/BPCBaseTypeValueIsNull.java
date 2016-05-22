package com.bargetor.nest.bpc.exception;

import com.bargetor.nest.common.bpc.bean.BPCResponseError;

/**
 * Created by Bargetor on 16/4/13.
 */
public class BPCBaseTypeValueIsNull extends BPCException {

    public BPCBaseTypeValueIsNull(String paramName){
        super(paramName + " base type value is null");
    }
}
