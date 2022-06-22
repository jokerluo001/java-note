package com.libra.collectionDemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/*
*   1.集合concurrentModificationException知道吗?
*   2.产生原因
*   3.如何解决?
* */
public class ConcurrentModificationExceptionDemo {
    public static void main(String[] args) {
        //并发修改异常演示一
       /* List<String> list = new ArrayList();
        for (int i = 0; i < 10; ++i) {
            new Thread(() -> {
                list.add("test");
                System.out.println(list);
            },"线程" + i).start();
        }*/
        //并发修改异常演示二
        List<String> list = new ArrayList();
        list.add("java");
        list.add("python");
        list.add("go");
        list.add(".net");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            if ("python".equals(next)) {
                list.add("c#");
            }
        }

        //这个不会产生异常
        /*for (int i = 0; i < list.size(); ++i) {
            if ("python".equals(list.get(i))) {
                list.add("c#");
            }
        }*/

    }
}
