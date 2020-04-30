package my.test.lib;

import java.util.Random;

public class MyClass {
    public static void main(String[] args) {
        Random random=new Random();
        int i = random.nextInt(13);
        System.out.println("中奖的是:"+i);
    }
}
