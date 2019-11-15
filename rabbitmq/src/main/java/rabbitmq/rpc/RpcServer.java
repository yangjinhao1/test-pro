package rabbitmq.rpc;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RpcServer {

	public static void main(String[] args) throws Exception {
		 ConnectionFactory connectionFactory = new ConnectionFactory();
		 connectionFactory.setHost("192.168.182.131");
		 connectionFactory.setPort(5672);
		 connectionFactory.setUsername("admin");
		 connectionFactory.setPassword("admin");
		 Connection connection = connectionFactory.newConnection();
		 Channel c = connection.createChannel();
		 //定义队列 
		 c.queueDeclare("rpc_queue", false,false, false, null);
		 c.queuePurge("rpc_queue");//清空队列
		 DeliverCallback deliverCallback = new DeliverCallback() {

			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				BasicProperties properties = message.getProperties();
				 String string = new String(message.getBody());
				 System.out.println("msg"+string);
			    String correlationId = properties.getCorrelationId();
			    System.out.println("correlationId"+correlationId);
			    String replyTo = properties.getReplyTo();
			    int parseInt = Integer.parseInt(string);
			    fbnq(parseInt);
				//设置发回响应的id, 与请求id一致, 这样客户端可以把该响应与它的请求进行对应
				BasicProperties replyProps = new BasicProperties.Builder()
						.correlationId(correlationId)
						
						.build();
				c.basicPublish("", replyTo, replyProps, string.getBytes());

			}
			 
		 };
		 CancelCallback cancelCallback = new CancelCallback() {
				@Override
				public void handle(String consumerTag) throws IOException {
				}
			};
			
			//消费者开始接收消息, 等待从 rpc_queue接收请求消息, 不自动确认
			c.basicConsume("rpc_queue", false, deliverCallback, cancelCallback);
	
		
		 
	}
	protected static int fbnq(int n) {
		if(n == 1 || n == 2) return 1;
		
		return fbnq(n-1)+fbnq(n-2);
	}

	
}
