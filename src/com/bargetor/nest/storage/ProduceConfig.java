package com.bargetor.nest.storage;

import com.bargetor.nest.common.check.param.ParamCheck;

/**
 * Created by Bargetor on 16/4/3.
 */
public class ProduceConfig {
    @ParamCheck(isRequired = true)
    private int count;
    @ParamCheck(isRequired = true)
    private Class<? extends ProduceCommand> producerClass;
    /**
     * 消费频率,即一秒内能处理多少个任务
     */
    @ParamCheck(isRequired = true)
    private double frequency = 1;

    private String cron;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Class<? extends ProduceCommand> getProducerClass() {
        return producerClass;
    }

    public void setProducerClass(Class<? extends ProduceCommand> producerClass) {
        this.producerClass = producerClass;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
