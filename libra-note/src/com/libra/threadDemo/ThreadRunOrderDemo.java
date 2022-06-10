package com.libra.threadDemo;

/*
* 保证线程1,2,3顺序执行
* */
public class ThreadRunOrderDemo {
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            System.out.println("I am Thread1");
        });

        Thread thread2 = new Thread(() -> {
            try {
                thread1.join();   //join方法  放弃当前线程cpu控制权,直到调用join方法的线程执行完毕.即保证同步
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("I am Thread2");
        });

        Thread thread3 = new Thread(() -> {
            try {
                thread2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("I am Thread3");
        });


        thread2.start();
        thread1.start();
        thread3.start();
    }
}
