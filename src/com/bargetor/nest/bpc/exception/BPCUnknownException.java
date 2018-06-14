package com.bargetor.nest.bpc.exception;

public class BPCUnknownException extends BPCException {
    public BPCUnknownException(){
        super(-1, "未知错误");
    }
}
