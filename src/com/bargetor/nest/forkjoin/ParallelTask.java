package com.bargetor.nest.forkjoin;

import com.bargetor.nest.common.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Bargetor on 16/5/6.
 */
public class ParallelTask<I, R> extends RecursiveTask<List<R>> {
    private Collection<I> input;
    private Worker<I, R> worker;


    public ParallelTask(Collection<I> input, Worker<I, R> worker){
        this.input = input;
        this.worker = worker;
    }

    @Override
    protected List<R> compute() {
        if(ArrayUtil.isCollectionNull(this.input))return null;
        List<ParallelTaskWorker<I, R>> parallelTaskWorkers = new ArrayList<>();
        List<R> result = new ArrayList<>();

        this.input.forEach(one -> {
            ParallelTaskWorker<I, R> parallelTaskWorker = new ParallelTaskWorker<>(one, this.worker);
            parallelTaskWorker.fork();
            parallelTaskWorkers.add(parallelTaskWorker);
        });

        parallelTaskWorkers.forEach(one -> {
            R subResult = one.join();
            if(subResult == null)return;
            result.add(subResult);
        });

        return result;
    }


    class ParallelTaskWorker<I, R> extends RecursiveTask<R>{
        private I input;
        private Worker<I, R> worker;

        public ParallelTaskWorker(I input, Worker<I, R> worker){
            this.input = input;
            this.worker = worker;
        }

        @Override
        protected R compute() {
            return this.worker.work(this.input);
        }
    }

    public interface Worker<T, V>{
        V work(T one);
    }
}
