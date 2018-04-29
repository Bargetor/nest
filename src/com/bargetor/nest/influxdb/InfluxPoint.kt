package com.bargetor.nest.influxdb

public annotation class InfluxPoint(
        val name: String = "",
        val measurement: String = "default")