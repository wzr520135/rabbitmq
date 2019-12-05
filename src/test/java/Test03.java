import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @auther wzr
 * @create 2019-12-05 17:07
 * @Description 发布订阅模式 消费者
 * @return
 */

public class Test03 {
    public static void main(String[] args) throws  Exception {

        //创建连接构建信道
        ConnectionFactory f=new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Connection conn = f.newConnection();
        Channel channel = conn.createChannel();
         //1 定义交换机
        channel.exchangeDeclare("logs", "fanout");
         //2 定义队列
            //queue 随机队列名 , 非持久的, 排他 ,自动删除
           //  channel.queueDeclare("", false, true, true, null);
            String queue=channel.queueDeclare().getQueue();
        //3 队列绑定到交换机
          channel.queueBind(queue, "logs","");
          //4消费数据
        DeliverCallback deliverCallback=new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                String msg=new String(message.getBody());
                System.out.println(msg);
                System.out.println("***********************");

            }
        };
        CancelCallback  cancelCallback=new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };
          channel.basicConsume(queue, true,deliverCallback,cancelCallback);




    }

}
