package com.libra.cas;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/*
 * 1.什么是CAS?
 *  a. Compare And Set(比较并交换)的缩写,是一条cpu并发原语,JUC下很多类都使用了CAS.
 *  b. 基本思路,这个地址上的值和期望的值相等,成功就修改并返回true,失败返回false
 * 2.CAS底层原理?
 *  a. 依靠UnSafe类,volatile关键字以及native方法.
 *  b. 这些原子类的CAS方法实际底层调用的是UnSafe类的本地方法.Java无法直接访问底层操作系统,依靠UnSafe类中的本地方法访问.
 *  c. volatile保证了可见性,如果主内存的值被改掉了,该线程工作内存一定知道.
 * 3.CAS的问题?
 *  a.ABA问题: 线程A,B同时对原子类AtomicInteger(200)进行操作;
 *             (1)线程A拿到值200被挂起;
 *             (2)线程拿到值200并修改为300,结束;
 *             (3)线程B又拿到值300将其改为200,结束;
 *             (4)A线程拿到cpu使用权,继续执行未完成的CAS,程序执行起来并无问题,结束(很明显,2,3这两步有问题)
 *  b.循环开销时间大
 *  c.只能保证一个共享变量的原子操作.
 * 4.问题如何解决?
 *  a. ABA问题解决: 添加版本号,如果A-B-A 变成了 A1-B1-A2  JDK1.5引入AtomicStampedReference解决,思想就是版本号
 *
 * */
public class CompareAndSetDemo {
    static AtomicInteger atomicInteger = new AtomicInteger(200);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("====================compareAndSet====================");
        new Thread(() -> {
            boolean b = atomicInteger.compareAndSet(200, 10);
            System.out.println(b);
        }).start();

        new Thread(() -> {
            boolean b = atomicInteger.compareAndSet(200, 30);
            System.out.println(b);
        }).start();
        System.out.println("current atomicInteger data --> " + atomicInteger.get());
        System.out.println("====================compareAndSet====================");


        //可以看到t3线程中途将值改为了2019,但是t4没有察觉到,所以最终的值还是修改为了2022
        System.out.println("====================ABA问题演示====================");
        new Thread(() -> {
            int data = atomicInteger.get();
            System.out.println("线程t3首次拿到的值--->" + data);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            atomicInteger.compareAndSet(data, 2019);
            System.out.println("t3修改过一次的值-->" + atomicInteger.get());
            atomicInteger.compareAndSet(atomicInteger.get(), data);
            System.out.println("t3将值修改回去---->" + atomicInteger.get());
        }, "t3").start();

        new Thread(() -> {
            int data = atomicInteger.get();
            System.out.println("线程t4拿到的值--->" + data);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            atomicInteger.compareAndSet(data, 2022);
            System.out.println("current atomicInteger data --> " + atomicInteger.get());
        }, "t4").start();
        Thread.sleep(4000);
        System.out.println("====================ABA问题演示====================");

        System.out.println("====================ABA问题解决====================");
        Integer initNumber = 0;
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference<>(initNumber, 1);

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            Integer reference = atomicStampedReference.getReference();
            System.out.println("线程t3首次拿到的值--->" + reference);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            atomicStampedReference.compareAndSet(reference, reference + 1, stamp, stamp + 1);
            System.out.println("t3修改过一次的值-->" + atomicStampedReference.getReference());
            atomicStampedReference.compareAndSet(atomicStampedReference.getReference(), atomicStampedReference.getReference() - 1, atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println("t3将值修改回去---->" + atomicStampedReference.getReference());
        }, "t5").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            Integer reference = atomicStampedReference.getReference();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            boolean b = atomicStampedReference.compareAndSet(reference, reference + 1, stamp, stamp + 1);
            System.out.println("ABA 问题解决,b的值应为false,b的实际值为----->" + b);
            System.out.println("current atomicInteger data --> " + atomicInteger.get());
        }, "t6").start();
        Thread.sleep(4000);
        System.out.println("====================ABA问题解决====================");
    }
}
