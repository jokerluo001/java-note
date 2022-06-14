package com.libra.template;


/*
* 利用了乘法分配律 a * b = a * ( c1 + c2)  b可以分解成二进制数,末尾要么0要么1,可以将乘法转成加法。
* */
public class FastMul {
    public static long fastMul(long a, long b){
        long ans = 0;
        while (b > 0) {
            if ((b & 1) == 1) ans += a;
            b >>= 1;
            a += a;
        }
        return ans;
    }

    public static void main(String[] args) {
        long l = fastMul(3, 4);
        long l1 = fastMul(41, 30);
        long l2 = fastMul(9, 4);
        long l3 = fastMul(0, 5);
        long l4 = fastMul(5, 0);
        System.out.println(l);
        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l3);
        System.out.println(l4);
    }
}
