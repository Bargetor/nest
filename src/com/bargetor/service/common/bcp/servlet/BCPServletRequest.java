package com.bargetor.service.common.bcp.servlet;

import com.bargetor.service.common.bcp.BCPUtil;
import com.bargetor.service.common.bcp.bean.BCPBaseRequestBody;
import com.bargetor.service.common.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Bargetor on 15/12/31.
 */
public class BCPServletRequest extends BodyReaderHttpServletRequestWrapper {

    private BCPBaseRequestBody requestBody;

    /**
     * 创建一个新的实例 BodyReaderHttpServletRequestWrapper.
     *
     * @param request
     * @throws IOException
     */
    public BCPServletRequest(HttpServletRequest request) throws IOException {
        super(request);

        this.requestBody = BCPUtil.buildBaseRequestBody(this);
        this.requestBody.setId(StringUtil.getUUID());
    }


    public BCPBaseRequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(BCPBaseRequestBody requestBody) {
        this.requestBody = requestBody;
    }
}
