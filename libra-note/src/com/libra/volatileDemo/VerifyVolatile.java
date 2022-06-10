package com.libra.volatileDemo;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

//volatile 是java提供的一个轻量级的同步机制
//1.保证了可见性  --> 解释什么是可见性?
//2.禁止指令重排  -->什么是指令重排?指令重排有什么坏处?
//3.不保证原子性  -->为什么?
//验证volatile保证可见性问题
public class VerifyVolatile {
    public static void main(String[] args) {
/*      //验证volatile保证可见性-----开始
        Data data = new Data();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            data.numberIncrement();
            System.out.println("线程结束");
        }).start();
        //主线程如果知道data.number改了,那么会跳出循环,结束运行;如果不知道,那么不会停止运行。
        while (data.getNumber() == 0) {}
        //打印 说明保证了可见性  不打印说明不保证
        System.out.println("系统停止了,表明main线程已经知道number进行过修改");
        //验证volatile保证可见性-----结束
*/


        //验证volatile不保证原子性-----开始
        /*
        * 解决方案
        * 1.对numberIncrement方法进行上锁  -->不推荐,虽然jdk1.6对synchronized进行了优化,但此处还是不推荐用这么重量级的锁。
        * 2.用juc下面的原子类AtomicInteger -->推荐
        * 3.ReentrantLock
        * */
        Data data2 = new Data();
        for (int i = 0; i < 20; ++i) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    data2.numberIncrement();
                }
            }).start();
        }
        //main线程和GC线程
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        //结果很大概率不会为20000,因为不保证原子性。
        System.out.println("计算结束,结果为" + data2.getNumber());
        //验证volatile不保证原子性-----结束

    }
}


class Data{
    //不加volatile关键字将会导致main线程不知道number值进行了修改从而陷入死循环。
    private volatile int number = 0;

    public void numberIncrement() {
        this.number++;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
