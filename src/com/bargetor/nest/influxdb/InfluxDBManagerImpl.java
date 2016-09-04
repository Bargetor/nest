package com.bargetor.nest.influxdb;

import com.bargetor.nest.common.check.param.ParamCheck;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.StringUtil;
import org.apache.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.impl.TimeUtil;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by Bargetor on 16/9/3.
 */
public class InfluxDBManagerImpl implements InitializingBean, InfluxDBManager {
    private static final Logger logger = Logger.getLogger(InfluxDBManagerImpl.class);

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
        if(!ParamCheckUtil.check(this) || StringUtil.isNullStr(this.serverUrl)){
            logger.error("influxdb params miss error", new Exception("influxdb params miss error"));
            return;
        }
        try {
            this.db = InfluxDBFactory.connect(this.serverUrl, this.userName, this.password);
            this.db.createDatabase(this.databaseName);
        }catch (Exception e){
            this.db = null;
            logger.error("influxdb connect error", e);
            return;
        }

        if(this.db == null){
            logger.error("influxdb unknow error");
            return;
        }
    }

    public void writePoint(Point point){
        if(this.db == null)return;
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
