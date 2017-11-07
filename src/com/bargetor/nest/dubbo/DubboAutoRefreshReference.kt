package com.bargetor.nest.dubbo

import com.alibaba.dubbo.common.utils.ConcurrentHashSet
import com.alibaba.dubbo.config.ReferenceConfig
import com.bargetor.nest.common.executor.ExecutorManager
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by bargetor on 2017/11/4.
 */
@Component
class DubboAutoRefreshReference: InitializingBean, DisposableBean{

    companion object {
        var isRefreshStop: Boolean = false
        val refreshReferences: MutableSet<DubboAutoReference<Any>> = ConcurrentHashSet()
        val dubboReferenceConfigMap: MutableMap<DubboAutoReference<Any>, ReferenceConfig<Any>> = ConcurrentHashMap()

        fun addRefresh(target: DubboAutoReference<Any>){
            DubboAutoRefreshReference.refreshReferences.add(target)
        }
    }

    fun dispatch(){
        DubboAutoRefreshReference.refreshReferences.forEach { target ->
            val config = DubboAutoRefreshReference.dubboReferenceConfigMap[target] ?: return@forEach
            try {
                val ref = config.get() ?: return@forEach
                target.referenceConfig = config

            }catch (e: Exception){}

        }
    }

    override fun afterPropertiesSet() {
        ExecutorManager.getInstance().commitRunnable({
            while (!isRefreshStop) {
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                }

                DubboAutoRefreshReference.refreshReferences.forEach { reference ->
                    try {
                        if (reference == null || reference.get() == null) {
                            DubboAutoRefreshReference.dubboReferenceConfigMap.put(
                                    reference,
                                    DubboUtil.createReferenceConfig(reference.interfaceClass, reference.version)
                            )
                        }
                    } catch (e: IllegalStateException) {
                    }
                }

                this.dispatch()

            }
        })
    }

    override fun destroy() {
        isRefreshStop = true
    }
}