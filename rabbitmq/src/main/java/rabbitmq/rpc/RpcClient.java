package rabbitmq.rpc;

import java.io.IOException;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.AMQP.BasicProperties.Builder;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class RpcClient {

	public static void main(String[] args) throws Exception {
		System.out.println("求第几个斐波那契数");
		int nextInt = new Scanner(System.in).nextInt();
		 long r=call(nextInt);
		 System.out.println(r);
	}

	private static long call(int nextInt) throws Exception {
		 ConnectionFactory connectionFactory = new ConnectionFactory();
		 connectionFactory.setHost("192.168.182.131");
		 connectionFactory.setPort(5672);
		 connectionFactory.setUsername("admin");
		 connectionFactory.setPassword("admin");
		 Connection connection = connectionFactory.newConnection();
		 Channel c = connection.createChannel();
		 ArrayBlockingQueue<Long> q=new ArrayBlockingQueue<>(1);
		 c.queueDeclare("rpc_queue", false, false, false, null);
		 String queue = c.queueDeclare().getQueue();
		  String cid = UUID.randomUUID().toString();
		 
		  String msg=""+nextInt;
		  BasicProperties prop = new BasicProperties.Builder().correlationId(cid).replyTo(queue).build();
		 c.basicPublish("", "rpc_queue", prop, msg.getBytes());
		 System.out.println("模拟执行其他运算,不必等待其他调用结果");
		 DeliverCallback deliverCallback = new DeliverCallback() {
			
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				if(cid.equals(message.getProperties().getCorrelationId())) {
					String string = new String(message.getBody());
					long fbnq = Long.parseLong(string);
				  q.offer(fbnq);
			}
		};
		
	};
	CancelCallback cancelCallback = new CancelCallback() {
		
		@Override
		public void handle(String consumerTag) throws IOException {
			// TODO Auto-generated method stub
			
		}
	};
	
	c.basicConsume(queue, true,deliverCallback, cancelCallback);
	Long take = q.take();
	connection.close();
 return take;
}
}