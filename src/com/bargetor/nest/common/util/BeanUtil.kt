package com.bargetor.nest.common.util

import org.springframework.beans.BeanUtils
import org.springframework.beans.BeanWrapperImpl
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

fun <T> observing(initialValue: T,
                  willSet: () -> Unit = { },
                  didSet: () -> Unit = { }
) = object : ObservableProperty<T>(initialValue) {
    override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
            true.apply { willSet() }

    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = didSet()
}

fun <T> observingOrNull(initialValueOrNull: T?,
                  willSet: () -> Unit = { },
                  didSet: () -> Unit = { }
) = object : ObservableProperty<T?>(initialValueOrNull) {
    override fun beforeChange(property: KProperty<*>, oldValue: T?, newValue: T?): Boolean =
            true.apply { willSet() }

    override fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T?) = didSet()
}

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