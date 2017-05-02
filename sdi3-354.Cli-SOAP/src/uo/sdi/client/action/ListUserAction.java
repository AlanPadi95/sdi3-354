package uo.sdi.client.action;

import java.util.List;

import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

import com.sdi.ws.AdminService;
import com.sdi.ws.EjbAdminServiceService;
import com.sdi.ws.UserTask;

public class ListUserAction implements Action {
	@Override
	public void execute() throws Exception {
		AdminService service = new EjbAdminServiceService()
				.getAdminServicePort();
		printHeader();
		List<UserTask> users = service.findAllUsersTask();
		for (UserTask u : users) {
			if (!u.isAdmin())
				Console.println("Login=" + u.getUsername() + " Email="
						+ u.getEmail() + " Estado=" + u.getStatus()
						+ "\n\t- Completadas=" + u.getTareasCompletadas()
						+ "\n\t- Completadas retrasadas= "
						+ u.getTareasCompletadasRetrasadas()
						+ "\n\t- Planificadas= " + u.getTareasPlanificadas()
						+ "\n\t- No planificadas=" + u.getTareasSinPlanificar());

			Log.debug("Se está mostrando el usuario " + u.getUsername());
		}
	}

	private void printHeader() {
		Console.println("Listar usuarios");
	}
}
