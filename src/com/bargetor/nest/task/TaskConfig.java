package com.bargetor.nest.task;

import com.bargetor.nest.common.check.param.ParamCheck;
/**
 * Created by Bargetor on 16/4/9.
 */
public class TaskConfig {
    /**
     * 消费线程数
     */
    @ParamCheck(isRequired = true)
    private int count = 1;

    /**
     * 消费频率,即一秒内能处理多少个任务
     */
    @ParamCheck(isRequired = true)
    private double frequency = 1;

    /**
     * 定时任务
     */
    private String cron;

    @ParamCheck(isRequired = true)
    private Class<? extends  TaskCommand> taskCommandClass;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public Class<? extends TaskCommand> getTaskCommandClass() {
        return taskCommandClass;
    }

    public void setTaskCommandClass(Class<? extends TaskCommand> taskCommandClass) {
        this.taskCommandClass = taskCommandClass;
    }
}
