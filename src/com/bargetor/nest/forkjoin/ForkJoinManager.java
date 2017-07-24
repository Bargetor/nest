package com.bargetor.nest.forkjoin;

import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.ArrayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created by Bargetor on 16/5/6.
 */
@Component
public class ForkJoinManager implements InitializingBean, DisposableBean {
    private static final Logger logger = Logger.getLogger(ForkJoinManager.class);
    private static ForkJoinManager instance;

    private ForkJoinPool forkJoinPool;
    private int poolSize = 4;

    public static ForkJoinManager getInstance(){
        if(instance == null){
            instance = (ForkJoinManager) SpringApplicationUtil.getBean(ForkJoinManager.class);
            if(instance == null){
                instance = new ForkJoinManager();
                instance.init();
            }
        }
        return instance;
    }

    public void init(){
        this.forkJoinPool = new ForkJoinPool(this.poolSize);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    public ParallelTasks parallelTasks(ParallelTask.WorkerForNull worker){
        ParallelTasks tasks = new ParallelTasks();
        if (worker == null)return tasks;
        tasks.add(worker);

        return tasks;
    }

    public ParallelTasks parallelTasks(ParallelTask.Worker worker){
        ParallelTasks tasks = new ParallelTasks();
        if (worker == null)return tasks;
        tasks.add(worker);

        return tasks;
    }

    /**
     * 平行处理
     * @param inputs
     * @param worker
     * @param <I>
     * @param <R>
     * @return
     */
    public <I, R>List<R> parallelTask(Collection<I> inputs, ParallelTask.Worker<I, R> worker){
        ParallelTask<I, R> parallelTask = new ParallelTask<>(inputs, worker);
        List<ParallelTask<I, R>.ParallelTaskWorker<I, R>> workers = parallelTask.getWorkers();
        if(ArrayUtil.isNull(workers))return null;

        List<Future<R>> future = this.forkJoinPool.invokeAll(workers);
        return ArrayUtil.list2List(future, one -> {
            try {
                return one.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e);
                return null;
            }
        });
    }


    public ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    public void setForkJoinPool(ForkJoinPool forkJoinPool) {
        this.forkJoinPool = forkJoinPool;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }


    public void destroy(){
        this.forkJoinPool.shutdown();
    }
}
