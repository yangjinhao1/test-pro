package rabbitmq.routing;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {

	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.182.131");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection con = f.newConnection();
		Channel c = con.createChannel();
		c.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);
		String queue = c.queueDeclare().getQueue();
		System.out.print("请输入空格隔开的绑定键");
		String s = new Scanner(System.in).nextLine();
		String[] strs = s.split("\\s+");
		for(String key:strs) {
			c.queueBind(queue, "direct_logs", key);
			}

		//收到消息后用来处理消息的回调对象
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody(), "UTF-8");
				String routingKey = message.getEnvelope().getRoutingKey();
				System.out.println("收到: "+routingKey+" - "+msg);
			}
		};
		CancelCallback cancel = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		c.basicConsume(queue, true, callback, cancel);
	}
}
