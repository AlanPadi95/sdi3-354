package uo.sdi.client.action;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import uo.sdi.business.AdminService;
import uo.sdi.dto.UserTask;
import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.Action;

public class ListUserAction implements Action {

	private static final String ADMIN_SERVICE_JNDI_KEY = "sdi3-354/"
			+ "sdi3-354.EJB/" + "EjbAdminService!"
			+ "uo.sdi.business.impl.admin.RemoteAdminService";

	private AdminService service;

	@Override
	public void execute() throws Exception {
		Context ctx = new InitialContext();
		this.service = (AdminService) ctx.lookup(ADMIN_SERVICE_JNDI_KEY);
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
