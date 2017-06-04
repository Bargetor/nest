package com.bargetor.nest.mybatis.handler;

import com.alibaba.fastjson.JSON;
import com.bargetor.nest.common.ui.Location;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bargetor on 2017/6/4.
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.OTHER})
@MappedTypes({Map.class})
public class JsonMapTypeHandler extends BaseTypeHandler<Map<String, String>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Map<String, String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, JSON.toJSONString(parameter));
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return JSON.parseObject(json, HashMap.class);
    }

    @Override
    public Map<String, String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return JSON.parseObject(json, HashMap.class);
    }

    @Override
    public Map<String, String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return JSON.parseObject(json, HashMap.class);
    }
}
