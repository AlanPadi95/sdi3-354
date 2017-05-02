package uo.sdi.model;

import uo.sdi.client.rest.TaskServicesRest;
import uo.sdi.client.rest.UserServicesRest;

public class AuxSingleton {
	private static AuxSingleton auxiliar = null;
	private TaskServicesRest taskService;
	private UserServicesRest userService;
	private User user;

	private AuxSingleton() {
	}

	public static AuxSingleton getInstance() {
		if (auxiliar == null) {
			auxiliar = new AuxSingleton();
		}
		return auxiliar;
	}

	public TaskServicesRest getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskServicesRest taskService) {
		this.taskService = taskService;
	}

	public UserServicesRest getUserService() {
		return userService;
	}

	public void setUserService(UserServicesRest userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
