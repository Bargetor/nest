package com.bargetor.nest.bpc.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bargetor.nest.bpc.exception.BPCBaseTypeValueIsNull;
import com.bargetor.nest.common.util.ReflectUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCServiceMethod {
    private static final Logger logger = Logger.getLogger(BPCServiceMethod.class);

    private String methodName;
    private Object service;
    private Method method;

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

    private Object[] buildInvokeParams(BPCRequestBean requestBean){
        Parameter[] parameters = this.method.getParameters();
        int paramsCount = parameters.length;
        if(paramsCount <= 0)return null;
        Object[] paramValues = new Object[paramsCount];
        JSONObject paramValueJson = JSON.parseObject(requestBean.getParams());
        for(int i = 0, len = paramsCount; i < paramsCount; i++){
            paramValues[i] = this.buildParamValue(paramValueJson, parameters[i]);
        }
        return paramValues;
    }

    private Object buildParamValue(JSONObject paramValueJson, Parameter parameter){
        if(paramValueJson == null || parameter == null)return null;
        String paramName = parameter.getName();
        ReflectUtil.BaseType baseType = ReflectUtil.whichBaseType(parameter.getType());
        Object value = null;

        if(baseType == null){
            //如果不是基础类型参数,则toBean
            value = JSON.parseObject(paramValueJson.toJSONString(), parameter.getType());

        }else{
            switch (baseType) {
                case String:
                    value = paramValueJson.getString(paramName);
                    break;
                case Boolean:
                    value = paramValueJson.getBoolean(paramName);
                    break;
                case Byte:
                    value = paramValueJson.getByte(paramName);
                    break;
                case Char:
                    value = paramValueJson.getString(paramName);
                    break;
                case Double:
                    value = paramValueJson.getDouble(paramName);
                    break;
                case Float:
                    value = paramValueJson.getFloat(paramName);
                    break;
                case Int:
                    value = paramValueJson.getInteger(paramName);
                    break;
                case Long:
                    value = paramValueJson.getLong(paramName);
                    break;
                case Short:
                    value = paramValueJson.getShort(paramName);
                    break;
                default:
                    value = null;
            }
            if(value == null){
                throw new BPCBaseTypeValueIsNull();
            }
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
}
