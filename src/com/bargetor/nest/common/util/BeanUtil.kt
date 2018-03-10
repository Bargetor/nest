package com.bargetor.nest.common.util

import org.springframework.beans.BeanUtils
import org.springframework.beans.BeanWrapperImpl

class BeanUtil{
    companion object {
        fun copyPropertiesWithoutNull(src: Any, target: Any){
            BeanUtils.copyProperties(src, target, *getNullPropertyNames(src))
        }

        fun getNullPropertyNames(source: Any): Array<String>{
            val src = BeanWrapperImpl(source)
            val pds = src.propertyDescriptors
            return pds.filter {
                src.getPropertyValue(it.name) == null
            }.map { it.name }.toTypedArray()
        }
    }
}