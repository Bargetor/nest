package com.bargetor.nest.storage;

import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.executor.RunableTask;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.StringUtil;
import com.bargetor.nest.storage.exception.NestStorageConfigException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Bargetor on 16/4/3.
 */
public class Storage implements InitializingBean{
    private static final Logger logger = Logger.getLogger(Storage.class);

    /**
     * 这里的manager没有使用,考虑是否拿掉
     */
    private StorageManager manager;
    private ConsumeConfig consumerConfig;
    private ProduceConfig producerConfig;
    private ExecutorService consumerExecutorService;
    private ExecutorService producerExecutorService;

    private ScheduledTaskRegistrar registrar;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    public void init(){
        this.initScheduledTaskRegistrar();
        this.initProducer();
        this.initConsumer();
        this.registrar.afterPropertiesSet();

    }

    private void initProducer(){
        if(!ParamCheckUtil.check(this.producerConfig))throw new NestStorageConfigException();
        this.producerExecutorService = Executors.newFixedThreadPool(this.producerConfig.getCount());

        if(StringUtil.isNullStr(this.producerConfig.getCron())){
            this.registrar.addFixedDelayTask(() -> this.commitProduceCommand(), (int)(1000D / this.producerConfig.getFrequency()));
        }else {
            this.registrar.addCronTask(() -> this.commitProduceCommand(), this.producerConfig.getCron());
        }
    }

    private void initConsumer(){
        if(!ParamCheckUtil.check(this.consumerConfig))throw new NestStorageConfigException();
        this.consumerExecutorService = Executors.newFixedThreadPool(this.consumerConfig.getCount());

        if(StringUtil.isNullStr(this.consumerConfig.getCron())){
            this.registrar.addFixedDelayTask(() -> this.commitConsumeCommand(), (int)(1000D / this.consumerConfig.getFrequency()));
        }else {
            this.registrar.addCronTask(() -> this.commitConsumeCommand(), this.consumerConfig.getCron());
        }
    }

    public void destroy(){
        this.producerExecutorService.shutdown();
        this.consumerExecutorService.shutdown();
    }

    public void commitConsumeCommand(){
        ConsumeCommand command = (ConsumeCommand) this.getRunableTaskInstance(this.consumerConfig.getConsumerClass());
        this.consumerExecutorService.execute(command);

    }

    public void commitProduceCommand() {
        ProduceCommand command = (ProduceCommand) this.getRunableTaskInstance(this.producerConfig.getProducerClass());
        this.producerExecutorService.execute(command);
    }


    private RunableTask getRunableTaskInstance(Class<? extends RunableTask> clazz){
        try {
            RunableTask runableTask = (RunableTask) SpringApplicationUtil.getBean(clazz);
            if(runableTask == null){
                runableTask = clazz.newInstance();
            }
            return runableTask;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("nest storage init produce command error", e);
            return null;
        }
    }

    private void initScheduledTaskRegistrar(){
        try {
            registrar = (ScheduledTaskRegistrar) SpringApplicationUtil.getBean(ScheduledTaskRegistrar.class);
        }catch (Exception e){
            registrar = null;
        }

        if(registrar == null){
            registrar = new ScheduledTaskRegistrar();
        }
    }



    /***************************************** getter and setter ********************************************/

    public StorageManager getManager() {
        return manager;
    }

    public void setManager(StorageManager manager) {
        this.manager = manager;
    }

    public ProduceConfig getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(ProduceConfig producerConfig) {
        this.producerConfig = producerConfig;
    }

    public ConsumeConfig getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(ConsumeConfig consumerConfig) {
        this.consumerConfig = consumerConfig;
    }


}
