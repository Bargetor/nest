package com.bargetor.nest.test.storage;

import com.bargetor.nest.storage.*;

/**
 * Created by Bargetor on 16/4/3.
 */
public class StorageTest {

    public static int productCount;

    public static void main(String[] args){
        ProduceConfig producerConfig = new ProduceConfig();
        producerConfig.setCount(2);
        producerConfig.setFrequency(3);
        producerConfig.setProducerClass(TestProducter.class);

        ConsumeConfig consumerConfig = new ConsumeConfig();
        consumerConfig.setCount(1);
        consumerConfig.setFrequency(4);
        consumerConfig.setConsumerClass(TestConsumer.class);
        consumerConfig.setCron("0 13 18 * * ?");

        Storage storage = new Storage();
        storage.setConsumerConfig(consumerConfig);
        storage.setProducerConfig(producerConfig);
        storage.init();
    }

}
