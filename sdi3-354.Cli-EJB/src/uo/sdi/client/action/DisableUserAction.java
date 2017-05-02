package uo.sdi.client.action;

import javax.naming.Context;
import javax.naming.InitialContext;

import uo.sdi.business.AdminService;
import uo.sdi.dto.User;
import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

public class DisableUserAction implements Action {
	private static final String ADMIN_SERVICE_JNDI_KEY = "sdi3-354/"
			+ "sdi3-354.EJB/" + "EjbAdminService!"
			+ "uo.sdi.business.impl.admin.RemoteAdminService";

	private AdminService service;

	@Override
	public void execute() throws Exception {
		Context ctx = new InitialContext();
		this.service = (AdminService) ctx.lookup(ADMIN_SERVICE_JNDI_KEY);
		printHeader();
		String login = Console.readString("Login del usuario a deshabilitar");
		User user = service.findUserByLogin(login);
		service.disableUser(user.getId());
		Log.debug("El usuario " + user.getLogin() + " ha sido deshabilitado");
	}

	private void printHeader() {
		Console.println("Deshabilitar usuario");
	}
}
