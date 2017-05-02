package uo.sdi.client.action;

import java.util.List;

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

public class FindTaskTodayAtrasadasAction implements Action {
	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String GTD_QUEUE = "jms/queue/GTDQueue";
	private Connection con;
	private TemporaryQueue tempQueue;
	private Session session;
	MessageProducer producer;
	MessageConsumer consumer;

	@Override
	public void execute() throws Exception {
		Console.println("Tareas planificadas para hoy y atrasadas");
		initialize();
		MapMessage msg = createMessage();
		msg.setJMSReplyTo(tempQueue);
		producer.send(msg);
		recibirMensaje();
		con.close();
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
		@SuppressWarnings("unchecked")
		List<String> listTask = (List<String>) response.getObject();
		showTareas(listTask);
	}

	private MapMessage createMessage() throws JMSException {
		MapMessage msg = session.createMapMessage();
		AuxSingleton aux = AuxSingleton.getInstance();
		msg.setString("command", "findTaskTodayAtrasadas");
		msg.setLong("userId", aux.getUserId());

		return msg;
	}

	private void showTareas(List<String> todayTask) {
		for (String t : todayTask) {
			System.out.println("\n\t" + t.toString());
		}

	}

}
