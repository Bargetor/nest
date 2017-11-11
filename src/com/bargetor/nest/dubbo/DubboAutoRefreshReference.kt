package com.bargetor.nest.dubbo

import com.alibaba.dubbo.common.utils.ConcurrentHashSet
import com.alibaba.dubbo.config.ProtocolConfig
import com.alibaba.dubbo.config.ReferenceConfig
import com.bargetor.nest.common.executor.ExecutorManager
import com.bargetor.nest.dubbo.DubboAutoRefreshReference.Companion.dubboReferenceConfigMap
import com.bargetor.nest.dubbo.DubboAutoRefreshReference.Companion.isRefreshStop
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.lang.Exception
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future

/**
 * Created by bargetor on 2017/11/4.
 */
@Component
class DubboAutoRefreshReference: InitializingBean, DisposableBean{

    companion object {
        var isRefreshStop: Boolean = false
        val refreshReferences: MutableSet<DubboAutoReference<Any>> = ConcurrentHashSet()
        val dubboReferenceConfigMap: MutableMap<DubboAutoReference<Any>, ReferenceConfig<Any>> = ConcurrentHashMap()

        var refreshTask: Future<*>? = null

        fun addRefresh(target: DubboAutoReference<Any>){
            DubboAutoRefreshReference.refreshReferences.add(target)
        }
    }

    private fun dispatch(){
        DubboAutoRefreshReference.refreshReferences.forEach { target ->
            val config = DubboAutoRefreshReference.dubboReferenceConfigMap[target] ?: return@forEach
            try {
                val ref = config.get() ?: return@forEach
                target.referenceConfig = config

            }catch (e: Exception){}

        }
    }

    override fun afterPropertiesSet() {
        refreshTask = ExecutorManager.getInstance().commitRunnable({
            while (!isRefreshStop) {
                try {
                    Thread.sleep(10000)
                } catch (e: InterruptedException) {
                }

                DubboAutoRefreshReference.refreshReferences.forEach { reference ->
                    try {
                        if (isRefreshStop) {return@forEach}
                        if (reference == null || reference.get() == null) {
                            val oldConfig = DubboAutoRefreshReference.dubboReferenceConfigMap[reference]
                            oldConfig?.destroy()

                            val newConfig = DubboUtil.createReferenceConfig(reference.interfaceClass, reference.version)
                            DubboAutoRefreshReference.dubboReferenceConfigMap.put(reference, newConfig)

                            //异步线程，避免在结束进程时多创建了链接，导致结束失败
                            if (!isRefreshStop){newConfig.destroy()}
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
        refreshTask?.cancel(true)
        refreshReferences.forEach { it.referenceConfig?.destroy() }
        dubboReferenceConfigMap.values.forEach { it.destroy() }
    }
}