package org.cckj.atomicandlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子变量计数器
 * @author Administrator
 *
 */
public class AtomicCount {
	
	public AtomicInteger count = new AtomicInteger();
	
	static class Job implements Runnable{
		private AtomicCount count;
		private CountDownLatch countDown;
		public Job(AtomicCount count, CountDownLatch countDown){
			this.count = count;
			this.countDown = countDown;
		}
		public void run() {
			boolean isSuccess = false; //标记原子操作是否成功
			while (!isSuccess) {
				int countValue = count.count.get();
				//compareAndSet 比较原来的值是否相等
				isSuccess = count.count.compareAndSet(countValue, countValue + 1);
			}
			countDown.countDown();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDown = new CountDownLatch(1500);
		AtomicCount count = new AtomicCount();
		ExecutorService ex = Executors.newFixedThreadPool(5); //线程池
		for (int i = 0; i < 1500; i++) {
			ex.execute(new Job(count, countDown)); //执行1500次
		}
		countDown.await();
		System.out.println(count.count); //实际并不会得到完整的 执行次数
		ex.shutdown();
		
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		System.out.println(methodName);
	}
}
