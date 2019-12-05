import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;


import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


/**
 * @auther wzr
 * @create 2019-12-05 14:32
 * @Description 工作队列模式 生产者
 *
 * @return
 */

public class test2 {


   public static void main(String[] args) throws IOException, TimeoutException {


       // 建立连接
       ConnectionFactory f = new ConnectionFactory();
       f.setHost("192.168.198.5");
       f.setPort(5672);
       f.setUsername("admin");
       f.setPassword("admin");

       //建立信道
       Connection conn = f.newConnection();
       Channel c = conn.createChannel();
       /*
        * 声明队列,会在rabbitmq中创建一个队列
        * 如果已经创建过该队列，就不能再使用其他参数来创建
        *
        * 参数含义:
        *   -queue: 队列名称
        *   -durable: 队列持久化,true表示RabbitMQ重启后队列仍存在
        *             false 表示队列不持久化 true 表示持久化队列
        *   -exclusive: 排他,true表示限制仅当前连接可用
        *   -autoDelete: 当最后一个消费者断开后,是否删除队列
        *   -arguments: 其他参数
        */

       //定义队列
       //c.queueDeclare("helloworld1", true, false, false, null);
       c.queueDeclare("helloworld2", true, false, false, null);
       while (true) {
           System.out.println("输入:");
           String msg = new Scanner(System.in).nextLine();
           //c.basicPublish("", "helloworld1", null, msg.getBytes());
            //消息持久化 将null修改
           c.basicPublish("", "helloworld2", MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());

           System.out.println("信息已发送");
       }

   }

  //保存数据  的要进行之久化消息  如果队里已经存在 队列属性 是不可以修改 要么新建一个队列
     //1. 队列持久化
     //2.消息持久化




}
