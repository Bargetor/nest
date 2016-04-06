package com.bargetor.nest.storage;

import java.util.Collection;

/**
 * Created by Bargetor on 16/4/3.
 */
public interface StorageManager<T> {
    /**
     * 是否可以进行生产
     * @return
     */
    public boolean canProduce();

    /**
     * 获取一定数量的产品
     * @param count
     * @return
     */
    public Collection<T> getPorduce(int count);

    /**
     * 获取剩余产品数量
     * @return
     */
    public int getProduceRemainingAmount();
}
