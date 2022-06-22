package com.libra.blockingQueueDemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueDemo {
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue(3);
        long now = System.currentTimeMillis();
        new Thread(() -> {
            while (System.currentTimeMillis() < now + 30) {
                boolean result = blockingQueue.offer(String.valueOf(System.currentTimeMillis()));
                if (result) {
                    System.out.println("添加成功");
                } else {
                    System.out.println("已阻塞");
                }
            }
        }, String.valueOf("t1")).start();


        new Thread(() -> {
            while (blockingQueue.size() > 0) {
                try {
                    String take = blockingQueue.take();
                    System.out.println(take);
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, String.valueOf("t2")).start();
    }
}
