package com.bargetor.nest.influxdb;

import com.bargetor.nest.common.check.param.ParamCheck;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.impl.TimeUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Bargetor on 16/9/3.
 */
public class InfluxDBManagerImpl implements InitializingBean, InfluxDBManager {

    @ParamCheck(isRequired = true)
    private String serverUrl;
    @ParamCheck(isRequired = true)
    private String userName;
    @ParamCheck(isRequired = true)
    private String password;
    @ParamCheck(isRequired = true)
    private String databaseName;
    private String retentionPolicy = "default";

    private InfluxDB db;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!ParamCheckUtil.check(this)){
            throw new Exception("influxdb params miss error");
        }

        this.db = InfluxDBFactory.connect(this.serverUrl, this.userName, this.password);
        if(this.db == null)throw new Exception("influxdb connect error");

        this.db.createDatabase(this.databaseName);
    }

    public void writePoint(Point point){
        this.db.write(this.databaseName, this.retentionPolicy, point);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getRetentionPolicy() {
        return retentionPolicy;
    }

    public void setRetentionPolicy(String retentionPolicy) {
        this.retentionPolicy = retentionPolicy;
    }
}
