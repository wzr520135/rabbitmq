import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @auther wzr
 * @create 2019-12-05 14:49
 * @Description
 * @return
 */

public class Test02 {
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory f=new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");
        Connection conn = f.newConnection();
        Channel c=conn.createChannel();
        DeliverCallback deliverCallback=new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
              String msg= new String(message.getBody());
                System.out.println("收到信息"+msg);
                for (int i = 0; i <msg.length() ; i++) {
                     char r=msg.charAt(i);
                     if('.'==r){
                         try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                     }
                    System.out.println("信息处理结果");
                    System.out.println("*********************");
                }



            }
        };
        CancelCallback callback=new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {


            }
        };
   /**
   * @Author: wzr
   * @Date: 2019/12/5 15:32
   * @Description:
      第二个参数:是否自动确认 ack
        true :表示服务器发送信息后,会直接删除数据
        false: -不自动确认,要等待消费者收到确认

    */
   //消费数据
      c.basicConsume("helloworld1", true,deliverCallback ,callback );

    }



}
