package com.bargetor.nest.config;

import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.task.TaskManager;
import com.bargetor.nest.task.TaskMapper;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Bargetor on 16/4/9.
 */
@Configuration
public class AppConfig{
    private static final Logger logger = Logger.getLogger(AppConfig.class);

    @Bean(name = "taskMapper")
    public MapperFactoryBean taskMapper(){
        MapperFactoryBean factoryBean = new MapperFactoryBean();
        factoryBean.setMapperInterface(TaskMapper.class);
        SqlSessionFactoryBean sqlSessionFactoryBean = (SqlSessionFactoryBean) SpringApplicationUtil.getBean(SqlSessionFactoryBean.class);
        try {
            factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
        } catch (Exception e) {
            logger.error(e);
        }

        return factoryBean;
    }

    @Bean(destroyMethod = "destroy")
    public TaskManager taskManager(){
        TaskManager taskManager = new TaskManager();
        try {
            taskManager.setTaskMapper((TaskMapper) taskMapper().getObject());
        } catch (Exception e) {
            logger.error(e);
        }
        return taskManager;
    }

}
