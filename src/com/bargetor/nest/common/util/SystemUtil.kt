package com.bargetor.nest.common.util

import com.google.common.collect.Range

@kotlin.jvm.JvmName("systemPrintRunTime")
public fun printRunTime(action: () -> Unit): Unit {
    val start = System.currentTimeMillis()
    action()
    println("run time:" + (System.currentTimeMillis() - start))
}