package com.bargetor.nest.bpc.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bargetor.nest.bpc.annotation.BPCParam;
import com.bargetor.nest.bpc.exception.BPCMethodParameterValueCheckFailError;
import com.bargetor.nest.common.check.param.ParamCheckList;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.util.MapUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCServiceMethod {
    private static final Logger logger = Logger.getLogger(BPCServiceMethod.class);

    private String methodName;
    private Object service;
    private Method method;

    private Map<Parameter, ParamCheckList> paramCheckListMap;

    private boolean isTest = false;

    public BPCServiceMethod(String methodName, Object service, Method method){
        this.methodName = methodName;
        this.service = service;
        this.method = method;
        this.buildParamCheckList(method);
    }

    public Object invoke(BPCRequestBean requestBean) throws Throwable {
        try {
            Object[] params = buildInvokeParams(requestBean);
            if(params == null){
                return this.method.invoke(this.service);
            }else{
                return this.method.invoke(this.service, params);
            }

        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            logger.error(String.format("bcp method {%s} invoke error", this.methodName), e);
            return null;
        }
    }

    private void buildParamCheckList(Method method){
        Parameter[] parameters = this.method.getParameters();
        if(parameters == null || parameters.length <= 0)return;

        this.paramCheckListMap = new HashMap<>();
        for (Parameter param : parameters) {
            BPCParam bpcParam = param.getAnnotation(BPCParam.class);
            if(bpcParam == null)continue;

            ParamCheckList checkList = new ParamCheckList();
            checkList.setRequired(bpcParam.isRequired());

            this.paramCheckListMap.put(param, checkList);
        }
    }

    private ParamCheckList getParamCheckList(Parameter param){
        if(param == null || MapUtil.isMapNull(this.paramCheckListMap))return null;
        return this.paramCheckListMap.get(param);
    }

    private Object[] buildInvokeParams(BPCRequestBean requestBean){
        Parameter[] parameters = this.method.getParameters();
        int paramsCount = parameters.length;
        if(paramsCount <= 0)return null;
        Object[] paramValues = new Object[paramsCount];
        JSONObject bpcParamJson = JSON.parseObject(requestBean.getParams());
        for(int i = 0, len = paramsCount; i < paramsCount; i++){
            paramValues[i] = this.buildParamValue(bpcParamJson, parameters[i]);
        }
        return paramValues;
    }

    private Object buildParamValue(JSONObject bpcParamsJson, Parameter parameter){
        if(bpcParamsJson == null || parameter == null)return null;
        String paramName = parameter.getName();
        Object value = bpcParamsJson.getObject(paramName, parameter.getType());

        ParamCheckList checkList = this.getParamCheckList(parameter);
        if(ParamCheckUtil.checkParamFail(value, checkList)){
            throw new BPCMethodParameterValueCheckFailError(paramName);
        }

        return value;
    }


    /***************************************** getter and setter **********************************/

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
