package uo.sdi.rest.impl;

import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.rest.UserServicesRest;

public class UserServiceRestImpl implements UserServicesRest {

	UserService userService = new Services().getUserService();// Factories.services.getUserService();

	@Override
	public User verifyUser(String login, String password)
			throws BusinessException {
		User user = userService.findLoggableUser(login, password);
		return user;
	}

}
