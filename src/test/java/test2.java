import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;


import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


/**
 * @auther wzr
 * @create 2019-12-05 14:32
 * @Description 工作队列模式
 * @return
 */

public class test2 {

   //@Test
   // public  void testP() throws IOException, TimeoutException {//生产者
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

       //定义队列
       c.queueDeclare("helloworld1", false, false, false, null);
       while (true) {
           System.out.println("输入:");
           String msg = new Scanner(System.in).nextLine();
           c.basicPublish("", "helloworld1", null, msg.getBytes());
           System.out.println("信息已发送");
       }

   }
   //}














}
