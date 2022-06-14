package com.libra.lockDemo;

import java.util.concurrent.atomic.AtomicReference;

/*
* 自旋锁----->如果持有锁的线程能在很短的时间内释放锁,那么等待竞争锁的线程就不需要做内核态到用户态之间的切换进入阻塞状态,只需要等一等(自旋)
* 优点：对锁竞争不激烈,且占用锁时间非常短的代码块性能可以大幅度提升（减少了线程上下文切换）
* 缺点：如果锁竞争激烈,且持有锁时间长就不适合自旋锁了,会占用cpu做无用功。
*
* 实现一个简单自旋锁
* */
public class MySpinLock {

    public static void main(String[] args) {
        SpinLockExample spinLockExample = new SpinLockExample();


        new Thread(() -> {
            spinLockExample.myLock();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            spinLockExample.myUnlock();
        }, "t1").start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            spinLockExample.myLock();
            spinLockExample.myUnlock();
        }, "t2").start();
    }

    static class SpinLockExample {
        AtomicReference<Thread> atomicReference = new AtomicReference<>();
        /*
        * 上锁
        * */
        public void myLock() {
            Thread thread = Thread.currentThread();
            while (!atomicReference.compareAndSet(null, thread)) {
                System.out.println(Thread.currentThread().getName() + "自旋获取锁...");
            }
        }

        /*
        * 解锁
        * */
        public void myUnlock() {
            Thread thread = Thread.currentThread();
            atomicReference.compareAndSet(thread, null);
        }
    }

}
