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
		 * 声明队列,会在rabbitmq中创建一个队列
		 * 如果已经创建过该队列，就不能再使用其他参数来创建
		 * 
		 * 参数含义:
		 *   1: 队列名称
		 *   2: 队列持久化,true表示RabbitMQ重启后队列仍存在
		 *  3: 排他,true表示限制仅当前连接可用
		 *  4: 当最后一个消费者断开后,是否删除队列
		 *  5: 其他参数
		 */
	  c.queueDeclare("helloworld", false, false, false, null);
	  //发送消息
		/*
		 * 发布消息
		 * 这里把消息向默认交换机发送.
		 * 默认交换机隐含与所有队列绑定,routing key即为队列名称
		 * 
		 * 参数含义:
		 * 	-exchange: 交换机名称,空串表示默认交换机"(AMQP default)",不能用 null 
		 * 	-routingKey: 对于默认交换机,路由键就是目标队列名称
		 * 	-props: 其他参数,例如头信息
		 * 	-body: 消息内容byte[]数组
		 */
	 
	  c.basicPublish("", "helloworld", null, ("hello world1 -"+System.currentTimeMillis()).getBytes());
	  System.out.println("消息已发送");
	  connection.close();
	}
}
