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
            TaskManager.getInstance().taskError(taskId, taskError);
        }
    }
}
