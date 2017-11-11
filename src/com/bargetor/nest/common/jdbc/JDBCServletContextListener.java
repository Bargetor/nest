package com.bargetor.nest.common.jdbc;

import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

/**
 * Created by bargetor on 2017/11/9.
 */
public class JDBCServletContextListener implements ServletContextListener {
    private static final Logger logger = Logger.getLogger(JDBCServletContextListener.class);
    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                logger.info(String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                e.printStackTrace();
                logger.error(String.format("deregistering jdbc driver: %s", driver));
            }
        }
    }
}
