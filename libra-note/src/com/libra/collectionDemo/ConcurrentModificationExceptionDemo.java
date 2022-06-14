package com.libra.collectionDemo;

import java.util.ArrayList;
import java.util.List;
/*
*   1.集合concurrentModificationException知道吗?
*   2.产生原因
*   3.如何解决?
* */
public class ConcurrentModificationExceptionDemo {
    public static void main(String[] args) {
        //并发修改异常演示
        List<String> list = new ArrayList();
        for (int i = 0; i < 10; ++i) {
            new Thread(() -> {
                list.add("test");
                System.out.println(list);
            },"线程" + i).start();
        }
    }
}
