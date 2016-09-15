package com.bargetor.nest.common.util;

/**
 * Created by Bargetor on 16/9/15.
 */
public class ExceptionUtil {

    public static String getExceptionStackTraceString(Throwable e){
        if(e == null)return null;
        StringBuffer buffer = new StringBuffer();
        ArrayUtil.listForeach(ArrayUtil.array2List(e.getStackTrace()), stackTrace ->{
            buffer.append(stackTrace.toString());
            buffer.append(System.getProperty("line.separator"));
        });
        return buffer.toString();
    }
}
