package uo.sdi.client.action;

import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

import com.sdi.ws.AdminService;
import com.sdi.ws.EjbAdminServiceService;
import com.sdi.ws.User;

public class DeepDeleteUserAction implements Action {
	@Override
	public void execute() throws Exception {
		AdminService service = new EjbAdminServiceService()
				.getAdminServicePort();
		printHeader();
		String login = Console.readString("Login del usuario a eliminar");
		User user = service.findUserByLogin(login);
		if (user != null) {
			service.deepDeleteUser(user.getId());
			Console.println("El usuario " + login + " ha sido eliminado");
			Log.debug("El usuario " + user.getLogin() + " ha sido eliminado");
		} else {
			Console.println("El usuario " + login + " no existe");
		}
	}

	private void printHeader() {
		Console.println("Eliminar usuario");
	}
}
