package rpc;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * @auther wzr
 * @create 2019-12-06 10:54
 * @Description
 * @return
 */

public class TestBlockingQueue {
     static BlockingQueue<Double> q=
              new ArrayBlockingQueue<Double>(10);

    public static void main(String[] args) {
         new Thread(){
             @Override
             public void run() {
                 Thread.currentThread().setName("线程A");
                //System.out.println("**********线程一****");
                 System.out.println(Thread.currentThread().getName());
                 System.out.println("按回车添加数据");
                  new Scanner(System.in).nextLine();
                  q.offer(Math.random()*100);//往集合里面添加数据
             }
         }.start();
         new Thread(){
             @Override
             public void run() {
                 Thread.currentThread().setName("线程B");
                 System.out.println(Thread.currentThread().getName());
                 try {
                     Double d = q.take();
                     System.out.println("得到的数是:"+d);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

             }
         }.start();


    }




}
