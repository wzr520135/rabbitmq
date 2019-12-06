package rpc;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.sun.deploy.security.TrustDecider;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @auther wzr
 * @create 2019-12-06 11:43
 * @Description rpc模式  客户端
 *
 * @return
 */

public class Test6 {

    static   BlockingQueue<Long> q=new ArrayBlockingQueue<Long>(10);

    //求第n个斐波那契数
    //1,1,2,3,5,8,13,21,24,34,55,89 f(n)=f(n-1)+f(n-2)(递归)效率慢;
    static long call(int n) throws Exception {
        //远程调用服务器的方法,计算斐波那契数

        // 建立连接构建通道
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();

        //定义调用队列
        c.queueDeclare("rpc_queue", false, false, false, null);
        //定义返回队列
        String returnQueue = c.queueDeclare().getQueue();
        //生成关联id
        String cid = UUID.randomUUID().toString();

        //设置两个参数:
        //1. 请求和响应的关联id
        //2. 传递响应数据的queue
        BasicProperties props = new BasicProperties.Builder()
                .correlationId(cid)
                .replyTo(returnQueue)
                .build();


      //  c.basicPublish("", "rpc_queue", null, ("" + n).getBytes());
        c.basicPublish("", "rpc_queue", props, ("" + n).getBytes());
        //可以先执行一些其他运算
        //1
        //2
        //3
        //...
        //直到需要计算结果时.再取数据
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
               //获取服务器返回结果,放入集合
                String msg=new String(message.getBody());//获取返回信息
                String id=message.getProperties().getCorrelationId();//获取关联id信息
                 if(id.equals(cid)){//返回id与发送的关联id 比较
                     q.offer(Long.parseLong(msg));//向集合中添加数据
                 }

                }
        };
        CancelCallback callback = new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {


            }
        };
            //消费者会新启动一个线程等待接受数据,收到数据后,会调用回调兑现处理数据
        c.basicConsume(returnQueue, true, deliverCallback, callback);

         //从集合获取数据
            return q.take();
    }
        public static void main (String[]args) throws  Exception{
            System.out.println("请输入斐波那契数:");
         int n= new Scanner(System.in).nextInt();
         long r=call(n);
            System.out.println(r);
        }

    }
