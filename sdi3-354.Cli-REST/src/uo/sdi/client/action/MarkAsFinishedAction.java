package uo.sdi.client.action;

import uo.sdi.client.rest.TaskServicesRest;
import uo.sdi.model.AuxSingleton;
import uo.sdi.model.User;
import alb.util.console.Console;
import alb.util.menu.Action;

public class MarkAsFinishedAction implements Action {

	@Override
	public void execute() throws Exception {
		TaskServicesRest taskServices = AuxSingleton.getInstance()
				.getTaskService();
		User user = AuxSingleton.getInstance().getUser();
		Console.println("Marcar tarea como finalizada");
		Long taskId = Console.readLong("Id de la tarea");
		taskServices.markAsFinished(taskId, user.getId());
	}

}
