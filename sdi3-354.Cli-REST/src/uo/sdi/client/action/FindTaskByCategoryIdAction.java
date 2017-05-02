package uo.sdi.client.action;

import java.util.List;

import uo.sdi.client.rest.TaskServicesRest;
import uo.sdi.model.AuxSingleton;
import uo.sdi.model.Task;
import uo.sdi.model.User;
import alb.util.console.Console;
import alb.util.date.DateUtil;
import alb.util.log.Log;
import alb.util.menu.Action;

public class FindTaskByCategoryIdAction implements Action {

	@Override
	public void execute() throws Exception {
		TaskServicesRest taskService = AuxSingleton.getInstance()
				.getTaskService();
		User user = AuxSingleton.getInstance().getUser();
		Long categoryId = Console.readLong("Id de la categoría");
		List<Task> tareas = taskService.findTaskByCategoryId(categoryId,
				user.getId());
		if (tareas != null) {
			Console.println("Tareas pendientes y atrasadas, ordenadas por fecha de más antiguo a más reciente");
			showTareas(tareas);
		} else {
			Console.println("No hay tareas para dicho usuario");
			Log.error("No hay tareas para dicho usuario");
		}
	}

	private void showTareas(List<Task> todayTask) {
		for (Task t : todayTask) {
			String planificada = "";
			String finalizada = "";
			String creacion = "";
			finalizada = assertFinishedNotNull(t, finalizada);
			planificada = assertPlannedNotNull(t, planificada);
			creacion = assertCreatedNotNull(t, creacion);
			printTask(t, planificada, finalizada, creacion);
		}

	}

	private void printTask(Task t, String planificada, String finalizada,
			String creacion) {
		Console.println();
		Console.println("Título: " + t.getTitle());
		Console.println("\t- Comentarios: " + t.getComments());
		Console.println("\t- Fecha de creación: " + creacion);
		Console.println("\t- Fecha de planificación: " + planificada);
		Console.println("\t- Fecha de finalización: " + finalizada);
		Console.println();
	}

	private String assertCreatedNotNull(Task t, String creacion) {
		if (t.getCreated() != null) {
			creacion = DateUtil.toString(t.getCreated());
		}
		return creacion;
	}

	private String assertPlannedNotNull(Task t, String planificada) {
		if (t.getPlanned() != null) {
			planificada = DateUtil.toString(t.getPlanned());
		}
		return planificada;
	}

	private String assertFinishedNotNull(Task t, String finalizada) {
		if (t.getFinished() != null) {
			finalizada = DateUtil.toString(t.getFinished());
		}
		return finalizada;
	}
}
