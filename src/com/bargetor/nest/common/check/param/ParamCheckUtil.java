package com.bargetor.nest.common.check.param;

import com.bargetor.nest.common.util.ReflectUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;

/**
 * Created by Bargetor on 16/3/15.
 */
public class ParamCheckUtil {
    private static final Logger logger = Logger.getLogger(ParamCheckUtil.class);

    /**
     * 通过标注验证参数bean的有效性
     * @param paramsBean
     * @param <T>
     * @return
     */
    public static <T>boolean check(T paramsBean){
        if(paramsBean == null)return false;
        Field[] fields = ReflectUtil.getAllFields(paramsBean.getClass());
        boolean isPass = true;

        for(Field field : fields){
            ParamCheck checkAnnotation = field.getAnnotation(ParamCheck.class);
            if(checkAnnotation == null)continue;
            boolean isRequired = checkAnnotation.isRequired();
            Object value = ReflectUtil.getProperty(paramsBean, field);
            if(isRequired && value == null){
                logger.info(String.format("the %s is required", field.getName()));
                return false;
            }
        }

        return isPass;
    }
}
