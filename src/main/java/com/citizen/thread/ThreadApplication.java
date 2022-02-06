package com.citizen.thread;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThreadApplication {

	public static void main(String[] args) {
//		implicitLock();
		explicitLock();
//		concurrentInteger();
	}

	private static void implicitLock() {
		Count count = new Count();
		int index = 100;

		Runnable task = new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < index; j++) {
					count.view();
				}
			}
		};

		for (int i = 0; i < index; i++) {
			Thread thread = new Thread(task);
			thread.start();
		}
	}

	private static void explicitLock() {
		Count count = new Count();
		int index = 100;

		Runnable task = new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < index; j++) {
					count.getLock().lock();
					count.view();
					count.getLock().unlock();
				}
			}
		};

		for (int i = 0; i < index; i++) {
			Thread thread = new Thread(task);
			thread.start();
		}
	}

	private static void concurrentInteger() {
		ConcurrentCount count = new ConcurrentCount();
		final int index = 100;

		Runnable task = new Runnable() {
			@Override
			public void run() {
				for (int j = 0; j < index; j++) {
					count.view();
				}
			}
		};

		for (int i = 0; i < index; i++) {
			Thread thread = new Thread(task);
			thread.start();
		}
	}
}

class ConcurrentCount {
	private AtomicInteger count = new AtomicInteger(1);
	public int view() {
		int increment = count.getAndIncrement();
		System.out.println("Thread = " + Thread.currentThread() + ", count = " + increment);
		return increment;
	}
}

class Count {
	private int count = 1;
	private Lock lock = new ReentrantLock();
	public synchronized int view() {
		System.out.println("Thread = " + Thread.currentThread() + ", count = " + count++);
		return count;
	}

	public Lock getLock() {
		return lock;
	}
}