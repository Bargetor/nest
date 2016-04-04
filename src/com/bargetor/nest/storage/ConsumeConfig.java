package com.bargetor.nest.storage;

import com.bargetor.nest.common.check.param.ParamCheck;

/**
 * Created by Bargetor on 16/4/3.
 */
public class ConsumeConfig {
    /**
     * 消费线程数
     */
    @ParamCheck(isRequired = true)
    private int count = 1;

    /**
     * 消费频率,即一秒内能处理多少个任务
     */
    @ParamCheck(isRequired = true)
    private int frequency = 1;

    /**
     * 定时任务
     */
    private String cron;

    @ParamCheck(isRequired = true)
    private Class<? extends ConsumeCommand> consumerClass;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Class<? extends ConsumeCommand> getConsumerClass() {
        return consumerClass;
    }

    public void setConsumerClass(Class<? extends ConsumeCommand> consumerClass) {
        this.consumerClass = consumerClass;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
