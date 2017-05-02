package uo.sdi.model;

import java.util.List;

import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class AuxSingleton {
	private static AuxSingleton auxiliar = null;
	// Mensajería
	private MessageProducer producer;
	private MessageConsumer consumer;
	private Session session;
	// Usuario en sesión
	private Long userId;
	private List<Task> todayTask;

	private AuxSingleton() {
	}

	public static AuxSingleton getInstance() {
		if (auxiliar == null) {
			auxiliar = new AuxSingleton();
		}
		return auxiliar;
	}

	public MessageProducer getProducer() {
		return producer;
	}

	public void setProducer(MessageProducer producer) {
		this.producer = producer;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public MessageConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Task> getTodayTask() {
		return todayTask;
	}

	public void setTodayTask(List<Task> todayTask) {
		this.todayTask = todayTask;
	}
}
