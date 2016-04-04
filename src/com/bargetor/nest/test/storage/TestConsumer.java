package com.bargetor.nest.test.storage;

import com.bargetor.nest.storage.ConsumeCommand;

/**
 * Created by Bargetor on 16/4/3.
 */
public class TestConsumer extends ConsumeCommand {
    public TestConsumer(){

    }

    @Override
    protected void execute() {
        StorageTest.productCount --;
        System.out.println("消费");
    }
}