package com.bargetor.service.bpc.manager;

import com.bargetor.service.bpc.bean.BPCServiceMethod;
import com.bargetor.service.bpc.servlet.BPCDispatcherServlet;
import com.bargetor.service.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatchManager {
    private static BPCDispatchManager instance;

    /**
     * <url, <method_name, method>>
     */
    private Map<String, Map<String, BPCServiceMethod>> mapping;

    protected BPCDispatchManager(){
        this.mapping = new HashMap<>();

        //init default mapping
        this.registerUrlMapping(BPCDispatcherServlet.BPC_DEFAULT_URL);
    }

    public static BPCDispatchManager getInstance(){
        if(instance == null){
            instance = new BPCDispatchManager();
        }
        return instance;
    }

    public boolean registerDefaultServiceMethod(BPCServiceMethod method){
        return this.registerMethod(BPCDispatcherServlet.BPC_DEFAULT_URL, method);
    }

    public BPCServiceMethod getDefaultServiceMethod(String methodName){
        return this.getServiceMethod(BPCDispatcherServlet.BPC_DEFAULT_URL, methodName);
    }

    public BPCServiceMethod getServiceMethod(String url,String methodName){
        if( StringUtil.isNullStr(methodName) || StringUtil.isNullStr(url))return null;
        Map<String, BPCServiceMethod> mapping = this.getUrlMapping(url);
        if(mapping == null)return null;
        return mapping.get(methodName);
    }

    public boolean registerMethod(String url, BPCServiceMethod method){
        if(method == null || StringUtil.isNullStr(url))return false;
        if(StringUtil.isNullStr(method.getMethodName()))return false;
        Map<String, BPCServiceMethod> mapping = this.getUrlMapping(url);
        if(this.containsMethod(mapping, method))return true;

        mapping.put(method.getMethodName(), method);
        return true;
    }

    private Map<String, BPCServiceMethod> getUrlMapping(String url){
        if(StringUtil.isNullStr(url))return null;
        return this.mapping.get(url);
    }

    private Map<String, BPCServiceMethod> registerUrlMapping(String url){
        if(StringUtil.isNullStr(url))return null;
        if(this.containsUrlMapping(url))return this.mapping.get(url);
        Map<String, BPCServiceMethod> mapping = new HashMap<>();
        this.mapping.put(url, mapping);
        return mapping;
    }

    public boolean containsUrlMapping(String url){
        return this.mapping.containsKey(url);
    }

    private boolean containsMethod(Map<String, BPCServiceMethod> mapping, BPCServiceMethod method){
        return mapping.containsKey(method.getMethodName());
    }

}
