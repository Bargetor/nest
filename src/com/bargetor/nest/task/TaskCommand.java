package com.bargetor.nest.task;

import com.bargetor.nest.common.executor.RunableTask;
import com.bargetor.nest.task.bean.Task;
import com.bargetor.nest.task.bean.TaskError;

import java.math.BigInteger;

/**
 * Created by Bargetor on 16/4/9.
 */
public abstract class TaskCommand implements Runnable{
    public abstract Task getOneTask();

    public abstract void execute(Task task);

    /**
     * 当任务在执行过程中发生了一个已知异常,并希望发生此异常后,任务可以重试
     * 可以重写此方法,并在返回值中返回希望重试的异常
     * @return
     */
    public Class<? extends Exception>[] getRetryExceptionClasses(){
        return new Class[]{};
    }

    @Override
    public final void run() {
        Task task = this.getOneTask();
        if(task == null || task.getTaskId() == null)return;

        BigInteger taskId = task.getTaskId();

        TaskManager.getInstance().taskRuning(taskId);
        try{
            this.execute(task);
            TaskManager.getInstance().taskDone(taskId);
        }catch (Exception e){
            TaskError taskError = new TaskError();
            taskError.setMsg(String.format("[%s]->%s", e.getClass().getName(), e.getMessage()));

            if(this.isInRetryExceptions(this.getRetryExceptionClasses(), e)){
                TaskManager.getInstance().taskRetry(taskId, taskError);
            }else{
                TaskManager.getInstance().taskError(taskId, taskError);
            }
        }
    }

    /**
     * 判断exception是否在retry exceptions中
     * @param retryExceptions
     * @param exception
     * @return
     */
    private boolean isInRetryExceptions(Class<? extends Exception>[] retryExceptions, Exception exception){
        if(retryExceptions == null || retryExceptions.length <= 0)return false;
        for (Class<? extends Exception> retryException : retryExceptions) {
            if(retryException.equals(exception.getClass()))return true;
        }
        return false;
    }
}
