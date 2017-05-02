package uo.sdi.business.integration;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.persistence.EntityExistsException;

import uo.sdi.business.TaskService;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.task.LocalTaskService;
import uo.sdi.business.impl.user.LocalUserService;
import uo.sdi.dto.Task;

@MessageDriven(activationConfig = { @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/GTDQueue") })
public class GTDListener implements MessageListener {
	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory factory;
	private Connection con;
	private Session session;
	@EJB(beanInterface = LocalTaskService.class)
	private TaskService taskService;
	@EJB(beanInterface = LocalUserService.class)
	private UserService userService;
	private MessageSingleton ms = MessageSingleton.getInstance();
	@Resource(mappedName = "java:/queue/LogQueue")
	private Destination logQueue;

	@Override
	public void onMessage(Message msg) {
		System.out.println("GTDListener: Msg received");

		try {
			process(msg);
		} catch (JMSException jex) {
			sendMapMessage(jex.getMessage());
			jex.printStackTrace();
		} catch (BusinessException e) {
			sendMapMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	private void process(Message msg) throws BusinessException, JMSException {
		if (!messageOfExpectedType(msg)) {
			System.out.println("Not of expected type " + msg);
			return;
		}
		ms.setQueue(msg.getJMSReplyTo());

		MapMessage mpmsg = (MapMessage) msg;

		String cmd = mpmsg.getString("command");
		if ("login".equals(cmd)) {
			doLoginUser(mpmsg);
		} else if ("findTaskTodayAtrasadas".equals(cmd)) {
			doFindTask(mpmsg);
		} else if ("markAsFinished".equals(cmd)) {
			doMarkAsFinished(mpmsg);
		} else if ("addTask".equals(cmd)) {
			doNewTask(mpmsg);
		}
	}

	private void sendMapMessage(String msgString) {
		try {
			con = factory.createConnection("sdi", "password");
			session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer sender = session.createProducer(logQueue);
			TextMessage msg = createTextMessage(msgString);
			sender.send(msg);
		} catch (JMSException jex) {

			jex.printStackTrace();
		} finally {
			close(con);
		}
	}

	private void close(Connection con) {
		try {
			con.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private TextMessage createTextMessage(String msgString) throws JMSException {
		// Creo un objeto ObjectMessaje para enviar la respuesta al servidor
		TextMessage msg = session.createTextMessage(msgString);
		return msg;
	}

	private void doFindTask(MapMessage msg) throws JMSException,
			BusinessException {
		Long userId = msg.getLong("userId");
		taskService.findTodayTasksByUserId(userId);
	}

	private void doMarkAsFinished(MapMessage msg) throws JMSException,
			EntityExistsException, BusinessException {
		Long taskId = msg.getLong("taskId");
		taskService.markTaskAsFinished(taskId);
	}

	private void doLoginUser(MapMessage msg) throws JMSException,
			EntityExistsException, BusinessException {
		String login = msg.getString("username");
		String password = msg.getString("password");
		userService.findLoggableUser(login, password);
	}

	private void doNewTask(MapMessage msg) throws JMSException,
			EntityExistsException, BusinessException {
		Task task = new Task();
		task.setTitle(msg.getString("title"));
		task.setComments(msg.getString("comment"));
		if (!msg.getString("categoryId").equals("Sin categoria"))
			task.setCategoryId(Long.parseLong(msg.getString("categoryId")));
		task.setCreated(alb.util.date.DateUtil.today());
		if (!msg.getString("planned").equals(""))
			task.setPlanned(alb.util.date.DateUtil.fromString(msg
					.getString("planned")));
		task.setUserId(msg.getLong("userId"));
		taskService.createTask(task);
	}

	private boolean messageOfExpectedType(Message msg) {
		return msg instanceof MapMessage;
	}
}