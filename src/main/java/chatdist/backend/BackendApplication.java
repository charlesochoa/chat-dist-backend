package chatdist.backend;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rabbitmq.client.ConnectionFactory;


@SpringBootApplication
public class BackendApplication {

	private static String userName = "byfntbvj";
	private static  String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
	private static String vHost  = "byfntbvj";
	private static String hostName = "kangaroo.rmq.cloudamqp.com";
	private static int portNumber = 5672;
	private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";


	public static void main(String[] args) {

		ConnectionFactory factory = new ConnectionFactory();
		SpringApplication.run(BackendApplication.class, args);
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost(vHost);
		factory.setHost(hostName);
		factory.setPort(portNumber);
		try {

			factory.setUri(uri);
			Connection conn = factory.newConnection();
			Channel channel = conn.createChannel();
			Thread.sleep(10000);
			channel.close();
			conn.close();
		} catch (Exception e){
			System.out.println(e);
		}
	}

}
