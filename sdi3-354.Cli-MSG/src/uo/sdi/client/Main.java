package uo.sdi.client;

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

import uo.sdi.client.action.AddTaskAction;
import uo.sdi.client.action.FindTaskTodayAtrasadasAction;
import uo.sdi.client.action.MarkAsFinishedAction;
import uo.sdi.listener.LogListener;
import uo.sdi.model.AuxSingleton;
import uo.sdi.util.Jndi;
import uo.sdi.util.LogConfig;
import alb.util.console.Console;
import alb.util.menu.BaseMenu;

public class Main extends BaseMenu {
	private static final String JMS_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	private static final String GTD_QUEUE = "jms/queue/GTDQueue";
	private static final String LOG_QUEUE = "jms/queue/LogQueue";
	private String password;
	private String username;
	private Connection con;
	private TemporaryQueue tempQueue;
	private Session session;
	MessageProducer producer;
	MessageConsumer consumer;

	public static void main(String[] args) {
		LogConfig.config();
		Main mainMenu = new Main();
		mainMenu.execute();
	}

	public Main() {
		this.username = Console.readString("Username");
		this.password = Console.readString("Password");
		try {
			initialize(username, password);
			MapMessage msg = createMessage();
			msg.setJMSReplyTo(tempQueue);
			producer.send(msg);
			recibirMensaje();
			con.close();
		} catch (JMSException | InterruptedException e) {
			e.printStackTrace();
		}

		menuOptions = new Object[][] {
				{ "Opciones: ", null },
				{ "Mostrar tareas de la pseudolista Hoy y atrasadas",
						FindTaskTodayAtrasadasAction.class },
				{ "Registrar una nueva tarea", AddTaskAction.class },
				{ "Marcar tarea como finalizada", MarkAsFinishedAction.class }, };
	}

	private void recibirMensaje() throws JMSException {
		consumer = session.createConsumer(tempQueue);
		// Asigno a la cola temporal el uo.sdi.listener del cliente
		ObjectMessage response = (ObjectMessage) consumer.receive(10000);
		guardarUserInSession(response);
	}

	private void guardarUserInSession(ObjectMessage response)
			throws JMSException {
		if (response == null) {
			System.out.println("Mensaje nulo");
		}
		Long user = (Long) response.getObject();
		if (user == null) {
			System.out.println("Usuario nulo");
		}
		AuxSingleton.getInstance().setUserId(user);
	}

	private void initialize(String username, String password)
			throws JMSException, InterruptedException {
		ConnectionFactory factory = (ConnectionFactory) Jndi
				.find(JMS_CONNECTION_FACTORY);
		Destination queue = (Destination) Jndi.find(GTD_QUEUE);
		Destination log = (Destination) Jndi.find(LOG_QUEUE);
		// Creo la conexión y la sesión para el usuario
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// Asigno el producer a la sesión y
		// creo una cola temporal que le asigno a sesión
		tempQueue = session.createTemporaryQueue();
		MessageConsumer consumer = session.createConsumer(log);
		consumer.setMessageListener(new LogListener());
		producer = session.createProducer(queue);
		con.start();
	}

	private MapMessage createMessage() throws JMSException {
		MapMessage msg = session.createMapMessage();

		msg.setString("command", "login");
		msg.setString("username", username);
		msg.setString("password", password);

		return msg;
	}
}