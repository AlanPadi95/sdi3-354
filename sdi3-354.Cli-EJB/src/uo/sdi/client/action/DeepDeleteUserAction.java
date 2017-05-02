package uo.sdi.client.action;

import javax.naming.Context;
import javax.naming.InitialContext;
import uo.sdi.business.AdminService;
import uo.sdi.dto.User;
import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

public class DeepDeleteUserAction implements Action {
	private static final String ADMIN_SERVICE_JNDI_KEY = "sdi3-354/"
			+ "sdi3-354.EJB/" + "EjbAdminService!"
			+ "uo.sdi.business.impl.admin.RemoteAdminService";

	private AdminService service;

	@Override
	public void execute() throws Exception {
		Context ctx = new InitialContext();
		try {
			this.service = (AdminService) ctx.lookup(ADMIN_SERVICE_JNDI_KEY);
			printHeader();
			String login = Console.readString("Login del usuario a eliminar");
			User user = service.findUserByLogin(login);
			service.deepDeleteUser(user.getId());
			Log.debug("El usuario " + user.getLogin() + " ha sido eliminado");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void printHeader() {
		Console.println("Eliminar usuario");
	}
}
