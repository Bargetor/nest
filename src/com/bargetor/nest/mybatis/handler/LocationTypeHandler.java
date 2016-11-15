package com.bargetor.nest.mybatis.handler;

import com.bargetor.nest.common.ui.Location;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.*;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bargetor on 2016/11/14.
 */
@MappedJdbcTypes({JdbcType.OTHER})
@MappedTypes({Location.class})
public class LocationTypeHandler extends BaseTypeHandler<Location>{

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Location parameter, JdbcType jdbcType) throws SQLException {
        try {
            WKTReader wktReader = new WKTReader();
            Geometry geometry = wktReader.read(parameter.toWKTString());
            Object pointObject = GeometryConverter.to(geometry);

            ps.setObject(i, pointObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Location getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Point point = (Point) GeometryConverter.from(rs.getObject(columnName));
        return this.point2Location(point);
    }

    @Override
    public Location getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Point point = (Point) GeometryConverter.from(rs.getObject(columnIndex));
        return this.point2Location(point);
    }

    @Override
    public Location getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Point point = (Point) GeometryConverter.from(cs.getObject(columnIndex));
        return this.point2Location(point);
    }

    private Location point2Location(Point point){
        if(point == null)return null;
        Location location = new Location();

        location.setLng(point.getX());
        location.setLat(point.getY());
        location.setType(Location.Type.GCJ02);

        return location;
    }
}
