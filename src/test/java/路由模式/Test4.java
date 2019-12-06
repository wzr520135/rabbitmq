package 路由模式;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @auther wzr
 * @create 2019-12-06 9:26
 * @Description  路由模式  生产者 只需要定义交换机
 * @return
 */

public class Test4 {

    public static void main(String[] args) throws  Exception {

  // 建立连接构建通道
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();

        c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
        while (true) {
            System.out.println("请输入:");
            String msg = new Scanner(System.in).nextLine();
            System.out.println("输入路由键:");
            String key = new Scanner(System.in).nextLine();
            //参数1: 交换机名
            //参数2: routingKey, 路由键,这里我们用日志级别,如"error","info","warning"
            //参数3: 其他配置属性
            //参数4: 发布的消息数据
            c.basicPublish("direct_logs", key, null, msg.getBytes());
            System.out.println("信息已发送");
        }

    }

}
