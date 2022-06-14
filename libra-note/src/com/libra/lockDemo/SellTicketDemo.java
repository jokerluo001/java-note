package com.libra.lockDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
*
* 多线程卖票问题  使用Lock上锁
* */
public class SellTicketDemo {
    public static void main(String[] args) {
        Example example = new Example(100);
        for (int i = 0; i < 3; ++i) {
            new Thread(() -> {
                example.sell();
            },String.valueOf(i)).start();
        }
    }


    static class Example {
        private int ticket;
        private final Lock lock = new ReentrantLock();
        private boolean hasTicket = true;

        public Example(int ticket) {
            this.ticket = ticket;
        }


        public void sell() {
            while (hasTicket) {
                lock.lock();
                if (ticket > 0) {
                    System.out.println(Thread.currentThread().getName() + "售出票号：" + ticket);
                    ticket--;
                }
                if (ticket == 0) {
                    hasTicket = false;
                }
                lock.unlock();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

