package uo.sdi.business.impl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import uo.sdi.business.AdminService;
import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.UserService;

public class LocalEjbServicesLocator extends Services {
	private static final String ADMIN_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbAdminService!"
			+ "uo.sdi.business.impl.admin.LocalAdminService";
	private static final String USER_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbUserService!"
			+ "uo.sdi.business.impl.admin.LocalUserService";
	private static final String TASK_SERVICE_JNDI_KEY = "java:global/"
			+ "sdi3-354/" + "sdi3-354.EJB/" + "EjbTaskService!"
			+ "uo.sdi.business.impl.admin.LocalTaskService";

	@Override
	public AdminService getAdminService() {
		try {
			Context ctx = new InitialContext();
			return (AdminService) ctx.lookup(ADMIN_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	public UserService getUserService() {
		try {
			Context ctx = new InitialContext();
			return (UserService) ctx.lookup(USER_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}

	public TaskService getTaskService() {
		try {
			Context ctx = new InitialContext();
			return (TaskService) ctx.lookup(TASK_SERVICE_JNDI_KEY);

		} catch (NamingException e) {
			throw new RuntimeException("JNDI problem", e);
		}
	}
}
