package com.bargetor.nest.common.util

import org.springframework.beans.BeanUtils
import org.springframework.beans.BeanWrapperImpl

class BeanUtil{
    companion object {
        fun copyPropertiesWithoutNull(src: Any, target: Any, vararg ignoreProperties: String){
            val nullPropertyNames = ArrayList(getNullPropertyNames(src))
            nullPropertyNames.addAll(ignoreProperties)

            BeanUtils.copyProperties(src, target, *(nullPropertyNames.toTypedArray()))
        }

        fun getNullPropertyNames(source: Any): List<String>{
            val src = BeanWrapperImpl(source)
            val pds = src.propertyDescriptors
            return pds.filter {
                src.getPropertyValue(it.name) == null
            }.map { it.name }
        }
    }
}