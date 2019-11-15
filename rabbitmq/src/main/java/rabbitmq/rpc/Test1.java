package rabbitmq.rpc;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class Test1 {

	public static void main(String[] args) {
		ArrayBlockingQueue<Double> q = new ArrayBlockingQueue<Double>(10);
		new Thread() {
			public void run() {
				System.out.println("按回车放数据");
				new Scanner(System.in).nextLine();
				double random = Math.random();
				q.offer(random);
				System.out.println("已添加数据");
			};
		}.start();
		
		new Thread() {
			public void run() {
				System.out.println("等待接收数据");
				try {
					Double take = q.take();
					System.out.println(take);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}
}
