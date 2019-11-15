package rabbitmq.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
		c.queueDeclare("task_queue", true, false, false, null);
	 DeliverCallback deliverCallback=new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				byte[] body = message.getBody();
				String string = new String (body);
				for(int i=0;i<string.length();i++) {
					if('.'==string.charAt(i)) {
						try {
							Thread.sleep(1000);
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				System.out.println(string);
				c.basicAck(message.getEnvelope().getDeliveryTag(), false);
				System.out.println("------------");
			}
		};
		CancelCallback cancelCallback= new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				c.basicQos(1);
				
			}
		};
		c.basicConsume("task_queue",false, deliverCallback, cancelCallback);
		
	}
}
