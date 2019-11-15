package rabbitmq.Exchanges;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

public class Test2 {

	public static void main(String[] args) throws Exception{
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setUsername("admin");
		f.setPassword("admin");
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//��������Ϊ logs �Ľ�����, ���������� fanout
		ch.exchangeDeclare("logs", "fanout");
		
		//�Զ����ɶ�����,
		//�ǳ־�,��ռ,�Զ�ɾ��
		String queueName = ch.queueDeclare().getQueue();
		
		//�Ѹö���,�󶨵� logs ������
		//���� fanout ���͵Ľ�����, routingKey�ᱻ���ԣ�������nullֵ
		ch.queueBind(queueName, "logs", "");
		
		System.out.println("�ȴ���������");
		
		//�յ���Ϣ������������Ϣ�Ļص�����
		DeliverCallback callback = new DeliverCallback() {
			@Override
			public void handle(String consumerTag, Delivery message) throws IOException {
				String msg = new String(message.getBody(), "UTF-8");
				System.out.println("�յ�: "+msg);
			}
		};
		
		//������ȡ��ʱ�Ļص�����
		CancelCallback cancel = new CancelCallback() {
			@Override
			public void handle(String consumerTag) throws IOException {
			}
		};
		
		ch.basicConsume(queueName, true, callback, cancel);
	}

}
