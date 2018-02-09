package com.bargetor.nest.dubbo

import com.alibaba.dubbo.config.ReferenceConfig

/**
 * Created by bargetor on 2017/11/4.
 */
@Deprecated("破坏了dubbo的断线重连机制，故废弃")
class DubboAutoReference<T>{
    var referenceConfig: ReferenceConfig<T>? = null

    var interfaceClass: Class<T>
    var version: String

    constructor(interfaceClass: Class<T>, version: String){
        this.interfaceClass = interfaceClass
        this.version = version
        DubboAutoRefreshReference.addRefresh(this as DubboAutoReference<Any>)
    }
    
    fun get(): T?{
        return this.referenceConfig?.get()
    }

}