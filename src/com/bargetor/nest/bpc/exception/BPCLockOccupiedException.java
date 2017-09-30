package com.bargetor.nest.bpc.exception;

/**
 * Created by Bargetor on 16/6/23.
 */
public class BPCLockOccupiedException extends BPCException {
    public BPCLockOccupiedException(){
        super("lock occupied");
    }
}
