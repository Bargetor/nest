package com.bargetor.nest.influxdb

import com.bargetor.nest.bpc.servlet.BPCDispatcherServlet.BPC_INFLUXDB_INVOKE_SUCCESS_TAG
import com.bargetor.nest.common.util.JoinPointUtil
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.influxdb.dto.Point
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.Resource

@Aspect
@Component
class InfluxDBPointManager {
    @Resource
    private lateinit var influxDBManager: InfluxDBManager

    @Around("@annotation(com.bargetor.nest.influxdb.InfluxPoint)")
    fun influxPoint(joinPoint: ProceedingJoinPoint): Any?{
        val startTime = System.currentTimeMillis()
        val returnResult = joinPoint.proceed(joinPoint.args)
        val runTime = System.currentTimeMillis() - startTime

        this.point(joinPoint, JoinPointUtil.getAnnotation(joinPoint, InfluxPoint::class.java), runTime)

        return returnResult
    }

    private fun point(joinPoint: JoinPoint, annotation: InfluxPoint, runtime: Long){
        var name = if (annotation.name.isEmpty()) joinPoint.signature.name else annotation.name
        name += ".runtime"
        val point = Point.measurement(annotation.measurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField(name, runtime)
                .build()

        this.influxDBManager.writePoint(point)
    }
}