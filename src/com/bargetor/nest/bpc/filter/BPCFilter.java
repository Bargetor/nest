package com.bargetor.nest.bpc.filter;

import com.bargetor.nest.bpc.servlet.BPCRequest;
import com.bargetor.nest.bpc.servlet.BPCResponse;

/**
 * Created by Bargetor on 16/4/14.
 */
public interface BPCFilter {

    boolean pass(BPCRequest request, BPCResponse response);
}
