package com.bargetor.nest.forkjoin;

import com.bargetor.nest.common.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Bargetor on 16/5/6.
 */
public class ParallelTask<I, R>{
    private Collection<I> input;
    private Worker<I, R> worker;


    public ParallelTask(Collection<I> input, Worker<I, R> worker){
        this.input = input;
        this.worker = worker;
    }

    public List<ParallelTaskWorker<I, R>> getWorkers() {
        if(ArrayUtil.isNull(this.input))return null;
        List<ParallelTaskWorker<I, R>> parallelTaskWorkers = new ArrayList<>();

        this.input.forEach(one -> {
            ParallelTaskWorker<I, R> parallelTaskWorker = new ParallelTaskWorker<>(one, this.worker);
            parallelTaskWorkers.add(parallelTaskWorker);
        });

        return parallelTaskWorkers;
    }


    class ParallelTaskWorker<I, R> implements Callable<R> {
        private I input;
        private Worker<I, R> worker;

        public ParallelTaskWorker(I input, Worker<I, R> worker){
            this.input = input;
            this.worker = worker;
        }

        @Override
        public R call() throws Exception {
            return this.worker.work(this.input);
        }
    }

    public interface Worker<T, V>{
        V work(T one);
    }
}
