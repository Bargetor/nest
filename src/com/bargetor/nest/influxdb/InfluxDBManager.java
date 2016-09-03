package com.bargetor.nest.influxdb;

import org.influxdb.dto.Point;

/**
 * Created by Bargetor on 16/9/3.
 */
public interface InfluxDBManager {
    public void writePoint(Point point);
}
