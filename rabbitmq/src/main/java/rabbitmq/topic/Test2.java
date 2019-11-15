package rabbitmq.topic;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {

	public static void main(String[] args) throws Exception {
		ConnectionFactory connectionFactory = new	ConnectionFactory();
		connectionFactory.setHost("192.168.182.131");
		connectionFactory.setPort(5672);
		connectionFactory.setUsername("admin");
		connectionFactory.setPassword("admin");
		Connection connection = connectionFactory.newConnection();
		Channel c= connection.createChannel();
		
		c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		//随机命名,非持久,独占,自动删除
		String queue = c.queueDeclare().getQueue();
		System.out.println("请输入绑定键,用空格隔开:");
		String s = new Scanner(System.in).nextLine();
		String[] str = s.split("\\s+");
		for(String key:str) {
			c.queueBind(queue, "topic_logs", key);
		}
		DeliverCallback deliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
			byte[] body = message.getBody();
			String string = new String(body);
			String routingKey = message.getEnvelope().getRoutingKey();
			System.out.println(routingKey+"-----"+string);
			System.out.println("---------------------");
			}
		};
		CancelCallback cancelCallback = new CancelCallback() {
			
			@Override
			public void handle(String consumerTag) throws IOException {
				// TODO Auto-generated method stub
				
			}
		};
		c.basicConsume(queue, true, deliverCallback,cancelCallback);
		
	}
}
