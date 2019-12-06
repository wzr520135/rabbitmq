import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * @auther wzr
 * @create 2019-12-06 9:37
 * @Description 路由模式 消费者 定义交换机  定义队列  绑定
 * @return
 */

public class Test04 {

    public static void main(String[] args) throws  Exception {
        ConnectionFactory f=new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();
         //定义交换机
        c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
          //定义队列 产生随机队列
        String queue = c.queueDeclare().getQueue();
        System.out.println("输入绑定键(用逗号隔开):");
        String s = new Scanner(System.in).nextLine();
        String[] a = s.split(",");
        for(String key :a){//将队列键绑定到交换机 指定路由键名
             c.queueBind(queue, "direct_logs", key);


        }

        DeliverCallback d=new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                     //消息数据 消息标签 消息的路由键
                String msg= new String( message.getBody());
                String key = message.getEnvelope().getRoutingKey();
                System.out.println("信息是"+msg);
                System.out.println("路由键:"+key );


            }
        };
        CancelCallback callback=new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };

     // 消费数据
          c.basicConsume(queue, true,d , callback);
        System.out.println("信息已消费");

    }



}
