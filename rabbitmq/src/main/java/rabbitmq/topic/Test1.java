package rabbitmq.topic;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {

	public static void main(String[] args) throws Exception, TimeoutException {
		 ConnectionFactory connectionFactory = new ConnectionFactory();
		 connectionFactory.setHost("192.168.182.131");
		 connectionFactory.setPort(5672);
		 connectionFactory.setUsername("admin");
		 connectionFactory.setPassword("admin");
		 Connection connection = connectionFactory.newConnection();
		 Channel c = connection.createChannel();
		 c.exchangeDeclare("topic_logs", BuiltinExchangeType.TOPIC);
		 while(true) {
			 System.out.println("消息:");
			 String msg=new Scanner(System.in).nextLine();
			 System.out.println("路由键:");
			 String key=new Scanner(System.in).nextLine();
			c.basicPublish("topic_logs", key, null,msg.getBytes());
		     System.out.println("--------------------------"); 
		 }
	}
}
