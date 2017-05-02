package uo.sdi.client.action;

import com.sdi.ws.AdminService;
import com.sdi.ws.EjbAdminServiceService;
import com.sdi.ws.User;

import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

public class DisableUserAction implements Action {
	@Override
	public void execute() throws Exception {
		AdminService service = new EjbAdminServiceService()
				.getAdminServicePort();
		printHeader();
		String login = Console.readString("Login del usuario a deshabilitar");
		User user = service.findUserByLogin(login);
		if (user != null) {
			service.disableUser(user.getId());
			Console.println("El usuario " + login + " ha sido deshabilitado");

			Log.debug("El usuario " + user.getLogin()
					+ " ha sido deshabilitado");
		} else {
			Console.println("El usuario " + login + " no existe");
		}
	}

	private void printHeader() {
		Console.println("Deshabilitar usuario");
	}
}
