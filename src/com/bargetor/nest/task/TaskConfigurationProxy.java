package com.bargetor.nest.task;

import com.bargetor.nest.common.util.ArrayUtil;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * Created by Bargetor on 16/4/9.
 */
public class TaskConfigurationProxy implements InitializingBean {
    private List<TaskConfig> configs;


    public List<TaskConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<TaskConfig> configs) {
        this.configs = configs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(ArrayUtil.isNull(this.configs))return;

        this.configs.forEach(config -> TaskManager.getInstance().commitTaskConfig(config));
    }
}
