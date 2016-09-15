package com.bargetor.nest.task;

import com.alibaba.fastjson.JSON;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.ObjectClone;
import com.bargetor.nest.common.util.StringUtil;
import com.bargetor.nest.forkjoin.ForkJoinManager;
import com.bargetor.nest.task.bean.Task;
import com.bargetor.nest.task.bean.TaskError;
import com.bargetor.nest.task.exception.NestTaskConfigException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Bargetor on 16/4/9.
 */
@Service
public class TaskManager implements InitializingBean{
    private final static Logger logger = Logger.getLogger(TaskManager.class);
    private static TaskManager instance;

    private TaskMapper taskMapper;
    private ExecutorService executorService;

    private ScheduledTaskRegistrar registrar;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    public static TaskManager getInstance(){
        if(instance == null){
            instance = (TaskManager) SpringApplicationUtil.getBean(TaskManager.class);
        }
        return instance;
    }

    protected final void init(){
        this.executorService = Executors.newCachedThreadPool();
        this.initScheduledTaskRegistrar();
    }

    protected final void initScheduledTaskRegistrar(){
        try {
            registrar = (ScheduledTaskRegistrar) SpringApplicationUtil.getBean(ScheduledTaskRegistrar.class);
        }catch (Exception e){
            registrar = null;
        }

        if(registrar == null){
            registrar = new ScheduledTaskRegistrar();
        }
    }

    public void commitTaskConfig(TaskConfig config){
        if(!ParamCheckUtil.check(config))throw new NestTaskConfigException();
        logger.info(String.format("task manager will commit config -> %s", config.toString()));

        if(StringUtil.isNullStr(config.getCron())){
            int delay = (int)(1000.0 / config.getFrequency());
            this.registrar.addFixedDelayTask(() -> this.commitTaskCommand(config), delay);
        }else {
            this.registrar.addCronTask(() -> this.commitTaskCommand(config), config.getCron());
        }
    }


    protected final void commitTaskCommand(TaskConfig config){
        if(config.getCount() <= 0 )return;

        //同步平行处理任务,保证任务不积压
        List<Object> inputs = new ArrayList<>();
        for (int i = 0; i < config.getCount(); i++) {
            inputs.add(new Object());
        }

        ForkJoinManager.getInstance().parallelTask(inputs, input -> {
            TaskCommand command = this.getRunableTaskInstance(config.getTaskCommandClass());
            command.run();
            return null;
        });


//        for (int i = 0; i < config.getCount(); i++) {
//            TaskCommand command = this.getRunableTaskInstance(config.getTaskCommandClass());
//            this.executeCommand(command);
//        }
    }

    public Task commitTask(String taskType, Object params, String tag){
        if(StringUtil.isNullStr(taskType))return null;

        Task task = new Task();
        task.setType(taskType);
        task.setTag(tag);
        if(params != null){
            task.setAttributeJson(JSON.toJSONString(params));
        }
        task.setStatus(Task.TaskStatus.created.name());

        task.setCreateTime(new Date());
        this.taskMapper.createTask(task);
        logger.info(String.format("the %s task builded -> %s", task.getType(), task.getTaskId().toString()));
        return task;
    }

    public void taskError(BigInteger taskId, TaskError error){
        if(taskId == null)return;
        this.taskMapper.updateTaskStatus(taskId, Task.TaskStatus.error.name());
        if(error != null){
            this.taskMapper.updateTaskErrorJson(taskId, JSON.toJSONString(error));
        }
        logger.info(String.format("the task error -> %s : %s", taskId.toString(), error.getMsg()));
    }

    public void taskRetry(BigInteger taskId, TaskError error){
        if(taskId == null)return;
        this.taskMapper.updateTaskStatus(taskId, Task.TaskStatus.retry.name());
        if(error != null){
            this.taskMapper.updateTaskErrorJson(taskId, JSON.toJSONString(error));
        }
        logger.info(String.format("the task retry -> %s , error msg is : %s", taskId.toString(), error.getMsg()));
    }

    public void taskRuning(BigInteger taskId){
        if(taskId == null)return;
        this.taskMapper.updateTaskStatus(taskId, Task.TaskStatus.running.name());
        logger.info(String.format("the task running -> %s", taskId.toString()));
    }

    public void taskDone(BigInteger taskId){
        if(taskId == null)return;
        this.taskMapper.updateTaskStatus(taskId, Task.TaskStatus.done.name());
        logger.info(String.format("the task done -> %s", taskId.toString()));
    }



    public <T>T getTaskParams(BigInteger taskId, Class<T> paramsClass){
        if(taskId == null)return null;
        String paramsJsonStr = this.taskMapper.getTaskAttributeJson(taskId);
        T params = JSON.parseObject(paramsJsonStr, paramsClass);
        return params;
    }

    public TaskError getTaskError(BigInteger taskId){
        if(taskId == null)return null;
        String errorJsonStr = this.taskMapper.getTaskErrorJson(taskId);
        TaskError error = JSON.parseObject(errorJsonStr, TaskError.class);
        return error;
    }

    public Task getOneCreatedTaskByType(String type){
        return this.getOneTask(type, Task.TaskStatus.created);
    }

    /**
     * 根据status的顺序获取task,直至有满足的task
     * @param type
     * @param status 必填, 状态优先序列,
     * @return
     */
    public Task getOneTask(String type, Task.TaskStatus... status){
        if(StringUtil.isNullStr(type))return null;
        if(status == null || status.length <= 0)return null;
        Task task = null;
        for (Task.TaskStatus state: status) {
            task = this.taskMapper.getOneTaskByTypeStatus(type, state.name());
            if(task != null){
                break;
            }
        }

        return task;
    }

    public void destroy(){
        this.executorService.shutdown();
    }

    private TaskCommand getRunableTaskInstance(Class<? extends TaskCommand> clazz){
        try {
            TaskCommand runableTask = (TaskCommand) SpringApplicationUtil.getBean(clazz);
            if(runableTask == null){
                runableTask = clazz.newInstance();
            }
            return runableTask;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("nest storage init produce command error", e);
            return null;
        }
    }

    public void executeCommand(Runnable command){
        if(command == null)return;
        this.executorService.execute(command);
    }

    /********************************* getter and setter ***********************************/

    public TaskMapper getTaskMapper() {
        return taskMapper;
    }

    public void setTaskMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
}
