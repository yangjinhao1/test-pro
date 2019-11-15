package rabbitmq.routing;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {

	public static void main(String[] args) throws Exception, TimeoutException {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.182.131");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		
	
		c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		while(true) {
			System.out.print("消息");
			String nextLine = new Scanner(System.in).nextLine();
			String msg = nextLine;
			System.out.print("路由键");
			String key= new Scanner(System.in).nextLine();
			c.basicPublish("direct_logs", key, null, msg.getBytes());
		}
	}
}
