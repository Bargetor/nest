package com.bargetor.nest.dubbo.error;

import com.bargetor.nest.bpc.exception.BPCException;

/**
 * Created by Bargetor on 16/10/11.
 */
public class NoDubboRegistryError extends BPCException{
    public NoDubboRegistryError(){
        super("this app not have dubbo registry config");
    }
}
