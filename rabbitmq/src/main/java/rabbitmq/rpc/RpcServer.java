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
		 //������� 
		 c.queueDeclare("rpc_queue", false,false, false, null);
		 c.queuePurge("rpc_queue");//��ն���
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
				//���÷�����Ӧ��id, ������idһ��, �����ͻ��˿��԰Ѹ���Ӧ������������ж�Ӧ
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
			
			//�����߿�ʼ������Ϣ, �ȴ��� rpc_queue����������Ϣ, ���Զ�ȷ��
			c.basicConsume("rpc_queue", false, deliverCallback, cancelCallback);
	
		
		 
	}
	protected static int fbnq(int n) {
		if(n == 1 || n == 2) return 1;
		
		return fbnq(n-1)+fbnq(n-2);
	}

	
}
