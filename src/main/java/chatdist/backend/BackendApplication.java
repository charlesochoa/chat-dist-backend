package chatdist.backend;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;


@SpringBootApplication
public class BackendApplication {

	private final static String QUEUE_NAME = "hola";
	private static String userName = "byfntbvj";
	private static  String password = "2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE";
	private static String vHost  = "byfntbvj";
	private static String hostName = "kangaroo.rmq.cloudamqp.com";
	private static int portNumber = 5672;
	private static String uri = "amqp://byfntbvj:2x_P1v83EjPv9MOr9ZEycnWq-ct7MDHE@kangaroo.rmq.cloudamqp.com/byfntbvj";


	public static void main(String[] args) throws Exception {

		ConnectionFactory factory = new ConnectionFactory();
		SpringApplication.run(BackendApplication.class, args);
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost(vHost);
		factory.setHost(hostName);
		factory.setPort(portNumber);
		try {

			factory.setUri(uri);
		} catch (Exception e){
			System.out.println(e);
			System.exit(-1);
		}

		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);

		int messageNumber;
		boolean end = false;

		do {
			System.out.println("Escribe un número y pulsa <Enter> para enviarlo. El 0 para finalizar.");
			Scanner sc = new Scanner(System.in);
			messageNumber = sc.nextInt();
			if (messageNumber == 0) {
				end = true;
			} else {
				String message = "Mensaje: " + messageNumber;
				// En el modelo de mensajería de RabbitMQ los productores nunca mandan mensajes
				// directamente a colas, siempre los publican a un exchange (centralita) que
				// los enruta a colas (por criterios definidos según el tipo de exchange).
				// En este caso, el string vacío (primer parámetro) identifica el "default" o
				// "nameless" exchange: los mensajes se enrutan a la cola indicad por
				// routingKey (segundo parámetro) si existe.
				channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
				System.out.println(" [x] Enviado '" + message + "'");
			}
		} while (!end);

		channel.close();
		conn.close();
	}

}
