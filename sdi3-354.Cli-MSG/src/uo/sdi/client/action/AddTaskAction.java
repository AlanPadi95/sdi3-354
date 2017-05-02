package uo.sdi.client.action;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import uo.sdi.model.AuxSingleton;
import uo.sdi.util.Jndi;
import alb.util.console.Console;
import alb.util.menu.Action;

public class AddTaskAction implements Action {

	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String GTD_QUEUE = "jms/queue/GTDQueue";
	private Connection con;
	private TemporaryQueue tempQueue;
	private Session session;
	MessageProducer producer;
	MessageConsumer consumer;

	@Override
	public void execute() throws Exception {
		Console.println("Añadir categoría");
		Console.println("Obligatorio (*)");
		String titulo = Console.readString("* Título");
		String comentarios = Console.readString("Comentarios");
		Long categoria = Console.readLong("Id de la categoría");
		String planificada = Console
				.readString("Fecha de planificación (Ej:30/05/2017)");
		initialize();
		MapMessage msg = createMessage(titulo, comentarios, categoria,
				planificada);
		msg.setJMSReplyTo(tempQueue);
		producer.send(msg);
		recibirMensaje();
		con.close();

	}

	private MapMessage createMessage(String titulo, String comentarios,
			Long categoria, String planificada) throws JMSException {
		AuxSingleton aux = AuxSingleton.getInstance();
		MapMessage msg = session.createMapMessage();
		Long user = aux.getUserId();

		msg.setString("command", "addTask");
		msg.setString("title", titulo);
		msg.setString("comments", comentarios);
		if (categoria != null)
			msg.setString("categoryId", categoria.toString());
		else {
			msg.setString("categoryId", "Sin categoria");
		}
		msg.setString("planned", planificada);
		msg.setLong("userId", user);

		return msg;
	}

	private void initialize() throws JMSException, InterruptedException {
		ConnectionFactory factory = (ConnectionFactory) Jndi
				.find(JMS_CONNECTION_FACTORY);
		Destination queue = (Destination) Jndi.find(GTD_QUEUE);
		// Creo la conexión y la sesión para el usuario
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Asigno el producer a la sesión y
		// creo una cola temporal que le asigno a sesión
		tempQueue = session.createTemporaryQueue();
		producer = session.createProducer(queue);
		con.start();
	}

	private void recibirMensaje() throws JMSException {
		consumer = session.createConsumer(tempQueue);
		// Asigno a la cola temporal el uo.sdi.listener del cliente
		ObjectMessage response = (ObjectMessage) consumer.receive(10000);
		String msg = (String) response.getObject();
		System.out.println(msg.toString());
	}
}
