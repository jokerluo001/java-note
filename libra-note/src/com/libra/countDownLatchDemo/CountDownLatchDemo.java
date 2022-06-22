package com.libra.countDownLatchDemo;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(3);
        WaitThread waitThread1 = new WaitThread("等待线程1",countDownLatch);
        WaitThread waitThread2 = new WaitThread("等待线程2", countDownLatch);

        WorkThread work1 = new WorkThread("工作线程1", countDownLatch);
        WorkThread work2 = new WorkThread("工作线程2", countDownLatch);
        WorkThread work3 = new WorkThread("工作线程3", countDownLatch);

        waitThread1.start();
        waitThread2.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        work1.start();
        work2.start();
        work3.start();
    }


    static class WaitThread extends Thread {
        private String name;
        private CountDownLatch c;

        public WaitThread(String name, CountDownLatch c) {
            this.name = name;
            this.c = c;
        }

        @Override
        public void run() {
            try {
                System.out.println(name + "进入等待");
                c.await();
                System.out.println(name + "继续执行");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class WorkThread extends Thread {
        private String name;
        private CountDownLatch c;

        public WorkThread(String name, CountDownLatch c) {
            this.name = name;
            this.c = c;
        }


        @Override
        public void run() {
            try {
                System.out.println(name + "开始执行");
                Thread.sleep(30);
                System.out.println("执行结束");
                c.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
