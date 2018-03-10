package com.bargetor.nest.bpc.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bargetor.nest.bpc.annotation.BPCParam;
import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCRequestMetaBean;
import com.bargetor.nest.bpc.exception.BPCLockGetException;
import com.bargetor.nest.bpc.exception.BPCLockOccupiedException;
import com.bargetor.nest.bpc.exception.BPCMethodParameterValueCheckFailError;
import com.bargetor.nest.bpc.exception.BPCRequestAuthException;
import com.bargetor.nest.common.check.param.ParamCheckList;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.MapUtil;
import com.bargetor.nest.common.util.StringUtil;
import org.apache.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private String lockKey;
    private String requestValidateEL;

    public BPCServiceMethod(String methodName, Object service, Method method){
        this.methodName = methodName;
        this.service = service;
        this.method = method;
        this.buildParamCheckList(method);
    }

    public Object invoke(BPCRequestBean requestBean) throws Throwable {
        try {
            LinkedHashMap<String, Object> paramMap = this.buildInvokeParams(requestBean);
            if (!this.requestValidate(this.addValidateNeedParamsTo(paramMap, requestBean))) throw new BPCRequestAuthException();
            return this.invokeByLockIfNeed(paramMap);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e) {
            logger.error(String.format("bcp method {%s} invoke error", this.methodName), e);
            return null;
        } catch (IllegalArgumentException e){
            logger.error(String.format("bcp method {%s} invoke error, because params is lost", this.methodName), e);
            return null;
        }
    }

    private Object invokeByLockIfNeed(LinkedHashMap<String, Object> paramMap) throws InvocationTargetException, IllegalAccessException {
        //不需要锁
        if(StringUtil.isNullStr(this.lockKey)){
            return this.invokeBasic(paramMap);
        }

        String realRockKey = this.buildLockKey(paramMap);
        if(StringUtil.isNullStr(realRockKey))throw new BPCLockGetException();

        RedissonClient redissonClient = (RedissonClient) SpringApplicationUtil.getBean(RedissonClient.class);

        //使用集群同步锁，为每一个任务上锁，避免重复计算
        RLock lock = redissonClient.getLock(realRockKey);
        boolean locked = false;
        try {
            locked = lock.tryLock(1000, 60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            //获取锁失败，直接退出
            throw new BPCLockGetException();
        }

        if(locked){
            Object result = this.invokeBasic(paramMap);
            lock.unlock();
            return result;
        }else{
            throw new BPCLockOccupiedException();
        }
    }

    private String buildLockKey(LinkedHashMap<String, Object> paramMap){
        if(StringUtil.isNullStr(this.lockKey))return null;

        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(paramMap);
        Expression exp = parser.parseExpression(this.lockKey);
        return exp.getValue(context, String.class);
    }

    private boolean requestValidate(LinkedHashMap<String, Object> paramMap){
        if(StringUtil.isNullStr(this.requestValidateEL))return true;

        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariables(paramMap);
        Expression exp = parser.parseExpression(this.requestValidateEL);
        return exp.getValue(context, Boolean.class);
    }


    private Object invokeBasic(LinkedHashMap<String, Object> paramMap) throws InvocationTargetException, IllegalAccessException {
        Object result;
        if(MapUtil.isMapNull(paramMap)){
            result = this.method.invoke(this.service);
        }else{
            result = this.method.invoke(this.service, paramMap.values().toArray());
        }
        return result;
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

    private LinkedHashMap<String, Object> buildInvokeParams(BPCRequestBean requestBean){
        Parameter[] parameters = this.method.getParameters();
        if(parameters.length <= 0)return null;
        LinkedHashMap<String, Object> paramMap = new LinkedHashMap<>();
        JSONObject bpcParamJson = JSON.parseObject(requestBean.getParams());
        for (Parameter parameter : parameters){
            Object paramValue = this.buildParamValue(requestBean, bpcParamJson, parameter);
            paramMap.put(parameter.getName(), paramValue);
        }
        return paramMap;
    }

    private LinkedHashMap<String, Object> addValidateNeedParamsTo(LinkedHashMap<String, Object> invokeParams, BPCRequestBean requestBean){
        LinkedHashMap<String, Object> allParams = new LinkedHashMap<>(invokeParams);
        allParams.put("meta", requestBean.getMeta());
        return allParams;
    }


    private Object buildParamValue(BPCRequestBean requestBean, JSONObject bpcParamsJson, Parameter parameter){
        if(bpcParamsJson == null || parameter == null)return null;

        BPCParam bpcParamAnnotation = parameter.getAnnotation(BPCParam.class);
        String paramName = parameter.getName();
        Object value = null;

        if(bpcParamAnnotation != null && bpcParamAnnotation.isAll()){
            value = bpcParamsJson.toJavaObject(parameter.getType());
        }else if(this.isMetaParamBeanType(parameter)){
            value = JSON.parseObject(JSON.toJSONString(requestBean.getMeta()), parameter.getType());
        }else {
            value = bpcParamsJson.getObject(paramName, parameter.getType());
        }

        ParamCheckList checkList = this.getParamCheckList(parameter);
        if(ParamCheckUtil.checkParamFail(value, checkList)){
            throw new BPCMethodParameterValueCheckFailError(paramName);
        }

        return value;
    }

    private boolean isMetaParamBeanType(Parameter parameter){
        return BPCRequestMetaBean.class.equals(parameter.getType());
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

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getRequestValidateEL() {
        return requestValidateEL;
    }

    public void setRequestValidateEL(String requestValidateEL) {
        this.requestValidateEL = requestValidateEL;
    }
}
