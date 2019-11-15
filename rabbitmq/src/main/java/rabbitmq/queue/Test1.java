package rabbitmq.queue;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Test1 {

	public static void main(String[] args) throws Exception {
	 ConnectionFactory connectionFactory = new ConnectionFactory();
	 connectionFactory.setHost("192.168.182.131");
	 connectionFactory.setPort(5672);
	 connectionFactory.setUsername("admin");
	 connectionFactory.setPassword("admin");
	 Connection connection = connectionFactory.newConnection();
	 Channel createChannel = connection.createChannel();
	 createChannel.queueDeclare("task_queue", true, false, false, null);
	 while(true) {
		 System.out.print(" ‰»Î");
		 String msg=new Scanner(System.in).nextLine();
		 createChannel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
	 }
	}
}
