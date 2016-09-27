package org.cckj.atomicandlock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 存在差异
 * @author Administrator
 *
 */
public class Count {

	public int count = 0;
	static class Job implements Runnable{
		private CountDownLatch countDown;
		private Count count;
		public Job(Count count, CountDownLatch countDown){
			this.count = count;
			this.countDown = countDown;
		}
		public void run() {
			count.count ++;
			countDown.countDown();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDown = new CountDownLatch(1500);
		Count count = new Count();
		ExecutorService ex = Executors.newFixedThreadPool(5); //线程池
		for (int i = 0; i < 1500; i++) {
			ex.execute(new Job(count, countDown)); //执行1500次
		}
		countDown.await();
		System.out.println(count.count); //实际并不会得到完整的 执行次数
		ex.shutdown();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
