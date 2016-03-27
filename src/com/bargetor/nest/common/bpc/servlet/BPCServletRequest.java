package com.bargetor.nest.common.bpc.servlet;

import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.bpc.bean.BPCBaseRequestBody;
import com.bargetor.nest.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Bargetor on 15/12/31.
 */
public class BPCServletRequest extends BodyReaderHttpServletRequestWrapper {

    private BPCBaseRequestBody requestBody;

    /**
     * 创建一个新的实例 BodyReaderHttpServletRequestWrapper.
     *
     * @param request
     * @throws IOException
     */
    public BPCServletRequest(HttpServletRequest request) throws IOException {
        super(request);

        this.requestBody = BPCUtil.buildBaseRequestBody(this);
        this.requestBody.setId(StringUtil.getUUID());
    }


    public BPCBaseRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(BPCBaseRequestBody requestBody) {
        this.requestBody = requestBody;
    }
}
