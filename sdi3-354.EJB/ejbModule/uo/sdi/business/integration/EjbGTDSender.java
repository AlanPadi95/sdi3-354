package uo.sdi.business.integration;

import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import uo.sdi.dto.Task;

@Stateless
public class EjbGTDSender implements GTDSender {

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory factory;
	@Resource
	private SessionContext ctx;
	private Connection con;
	private Session session;
	private MessageSingleton ms = MessageSingleton.getInstance();

	@Override
	public void send(String operation, Object obj) {
		try {
			audit(ctx.getCallerPrincipal(), operation, obj);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void audit(Principal principal, String operation, Object obj)
			throws JMSException {
		con = factory.createConnection("sdi", "password");
		session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Message msg = chooseAction(operation, obj);
		if (msg != null) {
			sendMapMessage(msg, obj);
			System.out.println("Msg sent with " + msg);
		}
	}

	/**
	 * Escoge la acción a realizar en función de la operación recibida
	 * 
	 * @param operation
	 * @return Un map con las claves de la respuesta y las respuestas
	 * @throws JMSException
	 */
	private Message chooseAction(String operation, Object obj)
			throws JMSException {
		Message msg = null;
		if (operation.equals("findLoggableUser")) {
			msg = crearMsg(operation, obj);
		} else if (operation.equals("findTodayTasksByUserId")) {
			List<String> tasks = convertToString(obj);
			msg = crearMsg(operation, tasks);
		} else if (operation.equals("markTaskAsFinished")) {
			msg = crearMsg(operation, "Tarea Finalizada");
		} else if (operation.equals("createTask")) {
			msg = crearMsg(operation, "Tarea Creada");
		}
		return msg;
	}

	@SuppressWarnings("unchecked")
	private List<String> convertToString(Object obj) {
		List<String> todayTask = new ArrayList<String>();
		for (Task t : (List<Task>) obj) {
			todayTask.add(t.toString());
		}
		return todayTask;
	}

	/**
	 * Crea el map de la respuesta
	 * 
	 * @param operation
	 *            operación relizada
	 * @param respuesta
	 *            respuesta al mensaje
	 * @return Map con la operación realizada y la respuesta al mensaje
	 * @throws JMSException
	 */
	private ObjectMessage crearMsg(String operation, Object respuesta)
			throws JMSException {
		// Creo un objeto ObjectMessaje para enviar la respuesta al servidor
		ObjectMessage objMessage = session.createObjectMessage();
		// Aquí va el objeto respuesta
		objMessage.setObject((Serializable) respuesta);
		objMessage.setObjectProperty("operacion", operation);
		return objMessage;
	}

	/**
	 * Envía el mensaje de respuesta como un objeto
	 * 
	 * @param msgMap
	 *            El map que contiene el mensaje
	 */
	private void sendMapMessage(Message msg, Object obj) {
		Destination queue = ms.getQueue();
		try {
			// El productor del mensaje Destination queue = Singleton.getQueue()
			MessageProducer sender = session.createProducer(queue);
			sender.send(msg);
		} catch (JMSException jex) {
			jex.printStackTrace();
		} finally {
			close(con);
		}
	}

	private void close(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();

		}
	}

}
