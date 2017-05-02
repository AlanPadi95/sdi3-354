package uo.sdi.business;

import uo.sdi.business.impl.admin.EjbAdminService;
import uo.sdi.business.impl.task.EjbTaskService;
import uo.sdi.business.impl.user.EjbUserService;

public class Services {

	public AdminService getAdminService() {
		return new EjbAdminService();
	}

	public UserService getUserService() {
		return new EjbUserService();
	}

	public TaskService getTaskService() {
		return new EjbTaskService();
	}

}
