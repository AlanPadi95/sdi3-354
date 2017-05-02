package uo.sdi.business.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.UserService;

public class RemoteEjbServicesLocator extends Services {
	private static final String ADMIN_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbAdminService!"
			+ "uo.sdi.business.impl.admin.RemoteAdminService";
	private static final String USER_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbUserService!"
			+ "uo.sdi.business.impl.user.RemoteUserService";
	private static final String TASK_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbTaskService!"
			+ "uo.sdi.business.impl.task.RemoteTaskService";

	@Override
	public AdminService getAdminService() {
		System.out.println("Using remote services locator");
		try {
			Context ctx = new InitialContext();
			return (AdminService) ctx.lookup(ADMIN_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	public UserService getUserService() {
		System.out.println("Using remote services locator");
		try {
			Context ctx = new InitialContext();
			return (UserService) ctx.lookup(USER_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	public TaskService getTaskService() {
		System.out.println("Using remote services locator");
		try {
			Context ctx = new InitialContext();
			return (TaskService) ctx.lookup(TASK_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

}
