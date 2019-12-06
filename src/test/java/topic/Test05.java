package topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * @auther wzr
 * @create 2019-12-06 10:27
 * @Description  主题模式 消费者
 * @return
 */

public class Test05 {
    public static void main(String[] args) throws  Exception {
        //建立连接构建信道
        ConnectionFactory f=new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();

         //定义交换机
        c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
        //产生随机队列
        String queue = c.queueDeclare().getQueue();
        //设置绑定键
        System.out.println("输入绑定键逗号隔开:");
        String s=new Scanner(System.in).nextLine();
        String[] keys = s.split(",");
        for(String key: keys){
            //将队里与交换机 路由键 绑定
             c.queueBind(queue, "topic_logs", key);
         }


        DeliverCallback deliverCallback=new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                 //输出绑定键
                String key = message.getEnvelope().getRoutingKey();
                System.out.println("绑定键是:"+key);
                //输出消息
                String msg = new String(message.getBody());
                System.out.println("消息是:"+msg);

            }
        };
        CancelCallback callback=new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };
        c.basicConsume(queue, false,deliverCallback,callback);
        System.out.println("信息已处理");


    }
}
