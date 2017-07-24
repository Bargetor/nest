package com.bargetor.nest.forkjoin;

import com.bargetor.nest.common.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bargetor on 2017/7/22.
 */
public class ParallelTasks {
    private List<ParallelTask> tasks = new ArrayList<>();

    public <I>ParallelTasks add(ParallelTask.WorkerForNull worker){
        return this.add(one -> {
            worker.work();
            return null;
        });
    }

    public <I, R>ParallelTasks add(ParallelTask.Worker<I, R> worker){
        return this.add((Collection<I>) ArrayUtil.array2List(new Object[]{new Object()}), worker);
    }

    public <I, R>ParallelTasks add(Collection<I> input, ParallelTask.Worker<I, R> worker){
        ParallelTask<I, R> task = new ParallelTask<I, R>(input, worker);
        this.tasks.add(task);
        return this;
    }

    public List<ParallelTask> getTasks() {
        return tasks;
    }

    public List<ParallelTask<Object, Object>.ParallelTaskWorker<Object, Object>> getWorkers(){
        if (ArrayUtil.isNull(this.tasks))return null;
        List<ParallelTask<Object, Object>.ParallelTaskWorker<Object, Object>> workers = new ArrayList<>();
        this.tasks.forEach(task -> {
            workers.addAll(task.getWorkers());
        });

        return workers;
    }

    public void invokeAll(){
        ForkJoinManager.getInstance().getForkJoinPool().invokeAll(this.getWorkers());
    }
}
