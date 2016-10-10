package com.bargetor.nest.bpc.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.bargetor.nest.bpc.bean.BPCResponseBean;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatcherServletProxy extends HttpServlet {
    static {
        //此处相当于一个启动预热
        //fast json 预热
        BPCResponseBean responseBean = new BPCResponseBean();
        responseBean.setId("fast json preheating");
        JSON.parseObject(JSON.toJSONString(responseBean), BPCResponseBean.class);
    }


    private BPCDispatcherServlet target;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BPCDispatcherServlet tempServlet = (BPCDispatcherServlet) SpringApplicationUtil.getBean(BPCDispatcherServlet.class);
        if(tempServlet != null){
            this.target = tempServlet;
        }else{
            this.target = new BPCDispatcherServlet();
        }
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        this.target.service(req, res);
    }
}
