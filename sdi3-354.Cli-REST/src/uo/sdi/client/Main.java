package uo.sdi.client;

import java.util.List;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import uo.sdi.client.action.AddTaskAction;
import uo.sdi.client.action.FindTaskByCategoryIdAction;
import uo.sdi.client.action.MarkAsFinishedAction;
import uo.sdi.client.rest.TaskServicesRest;
import uo.sdi.client.rest.UserServicesRest;
import uo.sdi.model.AuxSingleton;
import uo.sdi.model.Category;
import uo.sdi.model.User;
import uo.sdi.util.LogConfig;
import alb.util.console.Console;
import alb.util.log.Log;
import alb.util.menu.BaseMenu;

public class Main extends BaseMenu {
	// private static final String REST_SERVICE_URL = "http://localhost:8280"
	// + "/sdi3-354.Web" + "/rest/";
	private UserServicesRest userService;
	private TaskServicesRest taskService;
	private User user = null;

	public Main() {
		if (user == null) {
			run();
			AuxSingleton.getInstance().setTaskService(taskService);
			AuxSingleton.getInstance().setUserService(userService);
			menuOptions = new Object[][] {
					{ "Opciones: ", null },
					{ "Mostrar tareas pendientes y atrasadas de una categoría",
							FindTaskByCategoryIdAction.class },
					{ "Registrar una nueva tarea", AddTaskAction.class },
					{ "Marcar tarea como finalizada",
							MarkAsFinishedAction.class } };
		}
	}

	public static void main(String[] args) {
		LogConfig.config();
		Main mainMenu = new Main();
		mainMenu.execute();
	}

	public void run() {
		String username = Console.readString("Username");
		String password = Console.readString("Password");
		inicializeServices(username, password);
		this.user = userService.verifyUser(username, password);
		if (user != null && !user.getIsAdmin()) {
			AuxSingleton.getInstance().setUser(user);
			showCategories();
		} else {
			Log.error("No existe un usuario con login: " + username);
		}
	}

	private void showCategories() {
		List<Category> categorias = taskService.findCategoriesByUserId(user
				.getId());

		Console.println("Lista de categorías del usuario:");

		for (Category c : categorias) {
			Console.println("\t- Id:" + c.getId() + "  Nombre: " + c.getName());
		}
	}

	private void inicializeServices(String username, String password) {
		this.userService = new ResteasyClientBuilder().build()
				.register(new Authenticator(username, password))
				.target("http://localhost:8280/sdi3-354.Web/rest/")
				.proxy(UserServicesRest.class);
		this.taskService = new ResteasyClientBuilder().build()
				.register(new Authenticator(username, password))
				.target("http://localhost:8280/sdi3-354.Web/rest/")
				.proxy(TaskServicesRest.class);

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}