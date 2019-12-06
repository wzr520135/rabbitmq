package 发布订阅模式;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @auther wzr
 * @create 2019-12-05 17:00
 * @Description
 * @return   发布订阅模式  生产者
 */

public class Test3 {

    public static void main(String[] args) throws Exception {
         //创建连接构建信道
        ConnectionFactory f=new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Connection conn = f.newConnection();
        Channel channel = conn.createChannel();

        //定义名字为logs的交换机,交换机类型为fanout
        //这一步是必须的，因为禁止发布到不存在的交换。
           channel.exchangeDeclare("logs", "fanout");

          //发送数据
        while (true){
            System.out.println("输入:");
            String msg=new Scanner(System.in).nextLine();

            if ("exit".equals(msg)) {
                break;
            }
            //第一个参数,向指定的交换机发送消息
            //第二个参数,不指定队列,由消费者向交换机绑定队列
            //如果还没有队列绑定到交换器，消息就会丢失，
            //但这对我们来说没有问题;即使没有消费者接收，我们也可以安全地丢弃这些信息。
            channel.basicPublish("logs", "", null, msg.getBytes());
            System.out.println("信息已发送");


        }
          conn.close();

    }
}
