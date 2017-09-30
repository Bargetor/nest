package com.bargetor.nest.bpc.exception;

/**
 * Created by Bargetor on 16/6/23.
 */
public class BPCLockGetException extends BPCException {
    public BPCLockGetException(){
        super("lock exception");
    }
}
