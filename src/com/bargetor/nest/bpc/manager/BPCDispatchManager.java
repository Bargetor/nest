package com.bargetor.nest.bpc.manager;

import com.bargetor.nest.bpc.servlet.BPCDispatcherServlet;
import com.bargetor.nest.common.util.MapUtil;
import com.bargetor.nest.common.util.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.util.AntPathMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatchManager {
    private final static Logger logger = Logger.getLogger(BPCDispatchManager.class);
    private static AntPathMatcher pathMatcher = new AntPathMatcher();

    private static BPCDispatchManager instance;

    /**
     * <url_pattern, <method_name, method>>
     */
    private Map<String, Map<String, BPCServiceMethod>> mapping;

    protected BPCDispatchManager(){
        this.mapping = new HashMap<>();

        //init default mapping
        this.registerMappingForUrlPattern(BPCDispatcherServlet.BPC_DEFAULT_URL_PATTERN);
    }

    public static BPCDispatchManager getInstance(){
        if(instance == null){
            instance = new BPCDispatchManager();
        }
        return instance;
    }

    public boolean registerDefaultServiceMethod(BPCServiceMethod method){
        return this.registerMethod(BPCDispatcherServlet.BPC_DEFAULT_URL_PATTERN, method);
    }

    public BPCServiceMethod getDefaultServiceMethod(String methodName){
        return this.getServiceMethod(BPCDispatcherServlet.BPC_DEFAULT_URL_PATTERN, methodName);
    }

    public BPCServiceMethod getServiceMethod(String url, String methodName){
        if( StringUtil.isNullStr(methodName) || StringUtil.isNullStr(url))return null;
        Map<String, BPCServiceMethod> mapping = this.getMappingForUrl(url);
        if(mapping == null)return null;
        return mapping.get(methodName);
    }

    public boolean registerMethod(String urlPattern, BPCServiceMethod method){
        if(method == null || StringUtil.isNullStr(urlPattern))return false;
        if(StringUtil.isNullStr(method.getMethodName()))return false;
        Map<String, BPCServiceMethod> mapping = this.getMappingForUrlPattern(urlPattern);

        //url pattern 未注册，不允许注册method
        if(mapping == null)return false;

        if(this.containsMethod(mapping, method))return true;

        mapping.put(method.getMethodName(), method);
        logger.info(String.format("bpc start init service method {%s}", method.getMethodName()));
        return true;
    }

    private Map<String, BPCServiceMethod> getMappingForUrl(String url){
        if(StringUtil.isNullStr(url))return null;
        for (String urlPattern : this.mapping.keySet()) {
            if(pathMatcher.match(urlPattern, url))return this.mapping.get(urlPattern);
        }
        return null;
    }

    private Map<String, BPCServiceMethod> getMappingForUrlPattern(String urlPattern){
        if(StringUtil.isNullStr(urlPattern))return null;
        return this.mapping.get(urlPattern);
    }

    private Map<String, BPCServiceMethod> registerMappingForUrlPattern(String urlPattern){
        if(StringUtil.isNullStr(urlPattern))return null;
        if(this.containsMappingForUrlPattern(urlPattern))return this.mapping.get(urlPattern);
        Map<String, BPCServiceMethod> mapping = new HashMap<>();
        this.mapping.put(urlPattern, mapping);
        return mapping;
    }

    public boolean containsMappingForUrlPattern(String urlPattern){
        return this.mapping.containsKey(urlPattern);
    }

    public boolean containsMappingForUrl(String url){
        return this.getMappingForUrl(url) != null;
    }

    private boolean containsMethod(Map<String, BPCServiceMethod> mapping, BPCServiceMethod method){
        if(MapUtil.isMapNull(mapping))return false;
        return mapping.containsKey(method.getMethodName());
    }

}
