package rabbitmq.Exchanges;

import java.io.IOException;
import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test1 {

	public static void main(String[] args) throws Exception {
		ConnectionFactory f = new ConnectionFactory();
		f.setHost("192.168.64.140");
		f.setPort(5672);
		f.setUsername("admin");
		f.setPassword("admin");
		
		Connection c = f.newConnection();
		Channel ch = c.createChannel();
		
		//��������Ϊlogs�Ľ�����,����������Ϊfanout
		//��һ���Ǳ���ģ���Ϊ��ֹ�����������ڵĽ�����
		ch.exchangeDeclare("logs", "fanout");
		
		while (true) {
			System.out.print("������Ϣ: ");
			String msg = new Scanner(System.in).nextLine();
			if ("exit".equals(msg)) {
				break;
			}
			
			//��һ������,��ָ���Ľ�����������Ϣ
			//�ڶ�������,��ָ������,���������򽻻����󶨶���
			//�����û�ж��а󶨵�����������Ϣ�ͻᶪʧ��
			//�����������˵û������;��ʹû�������߽��գ�����Ҳ���԰�ȫ�ض�����Щ��Ϣ��
			ch.basicPublish("logs", "", null, msg.getBytes("UTF-8"));
			System.out.println("��Ϣ�ѷ���: "+msg);
		}

		c.close();
	}
	}
	


