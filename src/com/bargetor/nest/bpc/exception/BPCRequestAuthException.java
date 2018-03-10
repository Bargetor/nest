package com.bargetor.nest.bpc.exception;

public class BPCRequestAuthException extends BPCException {
    public BPCRequestAuthException(){
        super("request auth error");
    }
}
