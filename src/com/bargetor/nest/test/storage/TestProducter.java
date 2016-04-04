package com.bargetor.nest.test.storage;

import com.bargetor.nest.storage.ProduceCommand;

/**
 * Created by Bargetor on 16/4/3.
 */

public class TestProducter extends ProduceCommand {

    public TestProducter(){

    }

    @Override
    protected void execute() {
        StorageTest.productCount ++;
        System.out.println("生产");
    }
}