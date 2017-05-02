package uo.sdi.business.integration;

import javax.jms.Destination;

public class MessageSingleton {
	private static MessageSingleton auxiliar = null;
	private Destination queue;
	private String correlationID;

	private MessageSingleton() {
	}

	public static MessageSingleton getInstance() {
		if (auxiliar == null) {
			auxiliar = new MessageSingleton();
		}
		return auxiliar;
	}

	public Destination getQueue() {
		return queue;
	}

	public void setQueue(Destination queue) {
		this.queue = queue;
	}

	public String getCorrelationID() {
		return correlationID;
	}

	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}
}
