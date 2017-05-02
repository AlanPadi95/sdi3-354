package uo.sdi.business.impl.admin;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.admin.command.DeepDeleteUserCommand;
import uo.sdi.business.impl.admin.command.DisableUserCommand;
import uo.sdi.business.impl.admin.command.EnableUserCommand;
import uo.sdi.business.impl.admin.command.FindAllUsersTaskCommand;
import uo.sdi.business.impl.command.Command;
import uo.sdi.business.integration.GTDSender;
import uo.sdi.dto.User;
import uo.sdi.dto.UserTask;
import uo.sdi.persistence.Persistence;

/**
 * Session Bean implementation class EjbAdminService
 */
@Stateless
// @LocalBean
@WebService(name = "AdminService")
public class EjbAdminService implements RemoteAdminService, LocalAdminService {

	@EJB
	private GTDSender sender;

	@Override
	public void deepDeleteUser(Long id) throws BusinessException {
		new DeepDeleteUserCommand(id).execute();
	}

	@Override
	public void disableUser(Long id) throws BusinessException {
		new DisableUserCommand(id).execute();
	}

	@Override
	public void enableUser(Long id) throws BusinessException {
		new EnableUserCommand(id).execute();
	}

	@Override
	public List<User> findAllUsers() throws BusinessException {
		return new Command<List<User>>() {
			@Override
			public List<User> execute() throws BusinessException {
				return Persistence.getUserDao().findAll();
			}
		}.execute();
	}

	@Override
	public List<UserTask> findAllUsersTask() throws BusinessException {
		return new Command<List<UserTask>>() {
			@Override
			public List<UserTask> execute() throws BusinessException {
				return new FindAllUsersTaskCommand().execute();
			}
		}.execute();
	}

	@Override
	public User findUserById(final Long id) throws BusinessException {
		sender.send("findUserById", null);
		return new Command<User>() {
			@Override
			public User execute() throws BusinessException {
				return Persistence.getUserDao().findById(id);
			}
		}.execute();
	}

	@Override
	public User findUserByLogin(final String login) throws BusinessException {
		return new Command<User>() {
			@Override
			public User execute() throws BusinessException {
				return Persistence.getUserDao().findByLogin(login);
			}
		}.execute();
	}

	/**
	 * Default constructor.
	 */
	public EjbAdminService() {
		// TODO Auto-generated constructor stub
	}

}
