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

import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
@Component
public class ExecutorManager implements InitializingBean, DisposableBean {
	private static ExecutorManager instance;
	
	private ExecutorService executorService;
	
	
	public static ExecutorManager getInstance(){
		if(instance == null){
			instance = (ExecutorManager) SpringApplicationUtil.getBean(ExecutorManager.class);
			if (instance == null) instance = new ExecutorManager();
		}
		return instance;
	}
	
	/**
	 * 创建一个新的实例 ExecutorManager.
	 *
	 */
	public ExecutorManager() {
		this.executorService = Executors.newCachedThreadPool();
	}
	
	public void commitTask(RunableTask task){
		this.executorService.execute(task);
	}

	public Future<?> commitRunnable(Runnable runnable){
		return this.executorService.submit(runnable);
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void destroy() throws Exception {
		this.executorService.shutdown();
	}

	public static void main(String[] args){
		
	}

}
