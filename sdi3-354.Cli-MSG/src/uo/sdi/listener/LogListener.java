package uo.sdi.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import alb.util.log.Log;

public class LogListener implements MessageListener {

	@Override
	public void onMessage(Message msg) {
		try {
			processMessage(msg);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void processMessage(Message msg) throws JMSException {
		if (!(msg instanceof TextMessage)) {
			System.out.println("Message not of expected type");
			return;
		}

		TextMessage mmsg = (TextMessage) msg;
		System.out.println("Log message");
		Log.error(mmsg.getText());

	}

}
