package com.libra.cas;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * 1.什么是CAS?
 * 2.CAS底层原理?
 * 3.CAS的问题?
 * 4.问题如何解决?
 * */
public class CompareAndSetDemo {
    static AtomicInteger atomicInteger = new AtomicInteger(200);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            boolean b = atomicInteger.compareAndSet(200, 10);
            System.out.println(b);
        }).start();

        new Thread(() -> {
            boolean b = atomicInteger.compareAndSet(200, 10);
            System.out.println(b);
        }).start();
    }
}
