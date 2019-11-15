package rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {

	public static void main(String[] args) throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.182.131");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		Connection connection = connectionFactory.newConnection();
		Channel c = connection.createChannel();
		//队列名,持久,非独占,不自动删除,其他配置属性
		c.queueDeclare("task_queue", true, false, false, null);
	DeliverCallback deliverCallback	= new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
		    byte[] body = message.getBody();
		    String str=new String(body);
		    System.out.println(str);
		    c.basicAck(message.getEnvelope().getDeliveryTag(), false);
		    System.out.println("-------------------");
			}
		};
	CancelCallback cancelCallback= new CancelCallback() {
		
		@Override
		public void handle(String consumerTag) throws IOException {
			c.basicQos(1);
			
		}
	};
		c.basicConsume("task_queue", false,deliverCallback, cancelCallback);
	}
}
