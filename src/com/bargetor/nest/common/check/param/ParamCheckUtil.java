package com.bargetor.nest.common.check.param;

import com.bargetor.nest.common.util.MapUtil;
import com.bargetor.nest.common.util.ReflectUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bargetor on 16/3/15.
 */
public class ParamCheckUtil {
    private static final Logger logger = Logger.getLogger(ParamCheckUtil.class);


    public static <T>boolean check(T paramsBean){
        return check(paramsBean, false);
    }
    /**
     * 通过标注验证参数bean的有效性
     * @param paramsBean
     * @param isThrow 是否抛出错误
     * @param <T>
     * @return
     */
    public static <T>boolean check(T paramsBean, boolean isThrow){
        if(paramsBean == null)return false;
        Field[] fields = ReflectUtil.getAllFields(paramsBean.getClass());
        boolean isPass = true;

        Map<Field, Object> checkFailMap = new HashMap();
        for(Field field : fields){
            ParamCheck checkAnnotation = field.getAnnotation(ParamCheck.class);
            if(checkAnnotation == null)continue;
            boolean isRequired = checkAnnotation.isRequired();

            ParamCheckList checkList = new ParamCheckList();
            checkList.setRequired(isRequired);

            Object value = ReflectUtil.getProperty(paramsBean, field);
            if(!checkParam(value, checkList)) {
                checkFailMap.put(field, value);
            }
        }

        if (MapUtil.isMapNotNull(checkFailMap)){
            StringBuilder msgBuilder = new StringBuilder();
            checkFailMap.forEach((field, value) -> {
                String msg = String.format("the [%s.%s] = [%s] check fail", paramsBean.getClass().getName(), field.getName(), value);
                msgBuilder.append(msg);
                msgBuilder.append(System.lineSeparator());
            });
            if (isThrow) throw new ParamCheckError(msgBuilder.toString());
            logger.info(msgBuilder.toString());
            return false;
        }

        return isPass;
    }

    public static boolean checkParam(Object param, ParamCheckList checkList){
        if(checkList == null)return true;

        if(checkList.isRequired() && param == null)return false;

        return true;
    }

    public static <T>boolean checkFail(T paramsBean){
        return checkFail(paramsBean, false);
    }

    public static <T>boolean checkFail(T paramsBean, boolean isThrow){
        return !check(paramsBean, isThrow);
    }

    public static boolean checkParamFail(Object param, ParamCheckList checkList){return !checkParam(param, checkList);}
}
