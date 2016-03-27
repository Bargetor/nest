/**
 * bargetorCommon
 * com.bargetor.nest.common.executor
 * ExecutorManager.java
 * 
 * 2015年8月11日-下午11:06:23
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * ExecutorManager
 * 
 * kin
 * kin
 * 2015年8月11日 下午11:06:23
 * 
 * @version 1.0.0
 *
 */
public class ExecutorManager {
	private static ExecutorManager instance;
	
	private ExecutorService executorService;
	
	
	public static ExecutorManager getInstance(){
		if(instance == null){
			instance = new ExecutorManager();
		}
		return instance;
	}
	
	/**
	 * 创建一个新的实例 ExecutorManager.
	 *
	 */
	protected ExecutorManager() {
		this.executorService = Executors.newCachedThreadPool();
	}
	
	public void commitTask(RunableTask task){
		this.executorService.execute(task);
	}
	
	
	public static void main(String[] args){
		
	}

}
