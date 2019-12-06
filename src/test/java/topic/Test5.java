package topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * @auther wzr
 * @create 2019-12-06 10:21
 * @Description 主题模式 就是复杂的路由模式   生产者 使用专门的交换机 topic
 * @return
 */

public class Test5 {
    public static void main(String[] args) throws Exception{


        //建立连接构建通道
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();
        //定义交换机
         c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
           //发送数据

        while (true) {
            System.out.println("请输入路由键:");
            String key = new Scanner(System.in).nextLine();
            System.out.println("请输入信息:");
            String msg= new Scanner(System.in).nextLine();


            c.basicPublish("topic_logs",key,null,msg.getBytes());
            System.out.println("信息已发送");
        }
    }
}
