package uo.sdi.business.impl.user;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.user.command.FindLoggableUserCommand;
import uo.sdi.business.impl.user.command.RegisterUserCommand;
import uo.sdi.business.impl.user.command.UpdateUserDetailsCommand;
import uo.sdi.business.integration.GTDSender;
import uo.sdi.dto.User;

/**
 * Session Bean implementation class EjbUserService
 */
@Stateless
// @LocalBean
@WebService(name = "UserService")
public class EjbUserService implements RemoteUserService, LocalUserService {

	@EJB
	private GTDSender sender;

	@Override
	public Long registerUser(User user) throws BusinessException {
		// sender.send("registerUser", null);
		return new RegisterUserCommand(user).execute();
	}

	@Override
	public void updateUserDetails(User user) throws BusinessException {
		// sender.send("updateUserDetails", null);
		new UpdateUserDetailsCommand(user).execute();
	}

	@Override
	public User findLoggableUser(final String login, final String password)
			throws BusinessException {
		User user = new FindLoggableUserCommand<User>(login, password)
				.execute();
		if (sender != null) {
			sender.send("findLoggableUser", user.getId());
		}
		return user;
	}

	/**
	 * Default constructor.
	 */
	public EjbUserService() {
		// TODO Auto-generated constructor stub
	}

}
