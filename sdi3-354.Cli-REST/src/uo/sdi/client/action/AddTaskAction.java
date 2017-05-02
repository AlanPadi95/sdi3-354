package uo.sdi.client.action;

import java.util.Date;

import uo.sdi.client.rest.TaskServicesRest;
import uo.sdi.model.AuxSingleton;
import uo.sdi.model.Task;
import uo.sdi.model.User;
import alb.util.console.Console;
import alb.util.date.DateUtil;
import alb.util.menu.Action;

public class AddTaskAction implements Action {

	private TaskServicesRest taskService = AuxSingleton.getInstance()
			.getTaskService();
	private User user = AuxSingleton.getInstance().getUser();

	@Override
	public void execute() throws Exception {
		Console.println("Añadir categoría");
		Console.println("Obligatorio (*)");
		String titulo = Console.readString("* Título");
		String comentarios = Console.readString("Comentarios");
		Long categoria = Console.readLong("Id de la categoría");
		String planificada = Console
				.readString("Fecha de planificación (Ej:30/05/2017)");

		registrarTarea(titulo, comentarios, categoria, planificada);
	}

	private void registrarTarea(String titulo, String comentarios,
			Long categoria, String planificada) {
		Task task = new Task().setTitle(titulo).setUserId(user.getId())
				.setCategoryId(categoria).setComments(comentarios)
				.setCreated(DateUtil.today());

		if (!planificada.equals("")) {
			Date planned = DateUtil.fromString(planificada);
			task.setPlanned(planned);
		}

		taskService.addTask(task);
	}

}
