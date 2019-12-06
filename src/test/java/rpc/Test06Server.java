package rpc;

import com.rabbitmq.client.*;
import com.rabbitmq.client.AMQP.BasicProperties;
import java.io.IOException;

/**
 * @auther wzr
 * @create 2019-12-06 14:26
 * @Description rpc模式 服务端
 *  1.定义队列 2.从对了中获取请求数据(请求计算的信息, 关联id, 请求回调的队列) 3.发布请求计算之后的数据(同过从客户端放的请求回调队列)
 *
 * @return
 */

public class Test06Server {
    public static void main(String[] args) throws Exception {

        // 建立连接构建通道
        ConnectionFactory f = new ConnectionFactory();
        f.setHost("192.168.198.5");
        f.setPort(5672);
        f.setUsername("admin");
        f.setPassword("admin");

        Connection conn = f.newConnection();
        Channel c = conn.createChannel();


        /*
         * 定义队列 rpc_queue, 将从它接收请求信息
         *
         * 参数:
         * 1. queue, 对列名
         * 2. durable, 持久化
         * 3. exclusive, 排他
         * 4. autoDelete, 自动删除
         * 5. arguments, 其他参数属性
         */
        //1定义调用队列
          c.queueDeclare("rpc_queue", false, false, false, null);
           //1.1调用 清空队列
         c.queuePurge("rpc_queue");
        //2接受调用数据
        DeliverCallback deliverCallback= new DeliverCallback(){
            @Override
            public void handle(String consumerTag, Delivery message) throws IOException {
                //  消息数据 ,返回队列   关联id
                String msg=new String(message.getBody());//请求计算的信息
                String replyto=message.getProperties().getReplyTo();//请求回调的队列名字
                String cid=message.getProperties().getCorrelationId();//关联id
                Long r=fbnq(Integer.parseInt(msg));
                BasicProperties props=new BasicProperties().builder().replyTo(replyto)
                        .correlationId(cid).build();
                /*
                 * 发送响应消息
                 * 1. 默认交换机
                 * 2. 由客户端指定的,用来传递响应消息的队列名
                 * 3. 参数(关联id)
                 * 4. 发回的响应消息
                 */

                //3发送计算结果
                c.basicPublish("", replyto, props,(""+r).getBytes() );



            }
        };
        CancelCallback cancelCallback=new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {

            }
        };
        //2.1接受数据
          c.basicConsume("rpc_queue", true,deliverCallback,cancelCallback);

    }

    private static Long fbnq(int n) {
         if(n==1||n==2){
             return 1L;
         }
         return  fbnq(n-1)+fbnq(n-2);


    }


}
