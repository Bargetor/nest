package com.bargetor.nest.bpc.exception;

/**
 * Created by Bargetor on 16/6/23.
 */
public class BPCMethodNotFoundException extends BPCException {
    public BPCMethodNotFoundException(String methodName){
        super("Invalid request, not found method: " + methodName);
    }

}
