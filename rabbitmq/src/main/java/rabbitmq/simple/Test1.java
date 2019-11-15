package rabbitmq.simple;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {

	public static void main(String[] args) throws Exception {
	ConnectionFactory connectionFactory = new	ConnectionFactory();
	connectionFactory.setHost("192.168.182.131");
	connectionFactory.setPort(5672);
	connectionFactory.setUsername("admin");
	connectionFactory.setPassword("admin");
	Connection connection = connectionFactory.newConnection();
	  Channel c= connection.createChannel();
		/*
		 * ��������,����rabbitmq�д���һ������
		 * ����Ѿ��������ö��У��Ͳ�����ʹ����������������
		 * 
		 * ��������:
		 *   1: ��������
		 *   2: ���г־û�,true��ʾRabbitMQ����������Դ���
		 *  3: ����,true��ʾ���ƽ���ǰ���ӿ���
		 *  4: �����һ�������߶Ͽ���,�Ƿ�ɾ������
		 *  5: ��������
		 */
	  c.queueDeclare("helloworld", false, false, false, null);
	  //������Ϣ
		/*
		 * ������Ϣ
		 * �������Ϣ��Ĭ�Ͻ���������.
		 * Ĭ�Ͻ��������������ж��а�,routing key��Ϊ��������
		 * 
		 * ��������:
		 * 	-exchange: ����������,�մ���ʾĬ�Ͻ�����"(AMQP default)",������ null 
		 * 	-routingKey: ����Ĭ�Ͻ�����,·�ɼ�����Ŀ���������
		 * 	-props: ��������,����ͷ��Ϣ
		 * 	-body: ��Ϣ����byte[]����
		 */
	 
	  c.basicPublish("", "helloworld", null, ("hello world1 -"+System.currentTimeMillis()).getBytes());
	  System.out.println("��Ϣ�ѷ���");
	  connection.close();
	}
}
