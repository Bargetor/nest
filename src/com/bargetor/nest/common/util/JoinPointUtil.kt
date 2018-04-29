package com.bargetor.nest.common.util

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.reflect.MethodSignature

class JoinPointUtil{
    companion object {
        fun <T : Annotation> getAnnotation(joinPoint: JoinPoint, clazz: Class<T>): T {
            val sign = joinPoint.signature as MethodSignature
            val method = sign.method
            return method.getAnnotation(clazz)
        }
    }
}