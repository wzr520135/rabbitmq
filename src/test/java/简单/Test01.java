package 简单;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @auther wzr
 * @create 2019-12-05 14:04
 * @Description
 * @return 消费者 (简单模式)
 */

public class Test01 {
    public static void main(String[] args) throws IOException, TimeoutException {
        //连接
        ConnectionFactory f=new ConnectionFactory();
       f.setHost("192.168.198.5");
      f.setPort(5672);
      f.setUsername("admin");
      f.setPassword("admin");
        Connection conn=f.newConnection();
        Channel c = conn.createChannel();
        //定义队列

        //服务器端不存在队列,会新建队列
        //如果已经存在.相当于空操作
         c.queueDeclare("helloword", false, false, false, null);

       DeliverCallback deliverCallback  = new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
      byte[] body=message.getBody();
        String s=new String(body);
                System.out.println("收到信息"+s);

            }
        };
        CancelCallback cancelCallback= new CancelCallback(){
             @Override
             public void handle(String consumerTag) throws IOException {

             }
         };

        //在队列上等待接受数据
         //收到数据 处理数据信息对象
     //c.basicConsume("hello", true,处理信息回调对象,取消信息处理回调对象)
     c.basicConsume("helloword", true,deliverCallback,cancelCallback);

    }

}