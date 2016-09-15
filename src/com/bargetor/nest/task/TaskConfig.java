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
     * 延迟ms, 默认值为100, 避免无delay导致机器跑死
     */
    @ParamCheck(isRequired = true)
    private int delay = 100;

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

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
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

    @Override
    public String toString() {
        return "TaskConfig{" +
                "count=" + count +
                ", delay=" + delay +
                ", cron='" + cron + '\'' +
                ", taskCommandClass=" + taskCommandClass +
                '}';
    }
}
