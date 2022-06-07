package com.libra.volatileDemo;


//验证validate保证可见性问题
public class VerifyVolatile {
    public static void main(String[] args) {
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
