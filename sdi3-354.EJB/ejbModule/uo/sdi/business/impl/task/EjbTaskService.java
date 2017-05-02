package uo.sdi.business.impl.task;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.command.Command;
import uo.sdi.business.impl.task.command.CreateCategoryCommand;
import uo.sdi.business.impl.task.command.CreateTaskCommand;
import uo.sdi.business.impl.task.command.DeleteCategoryCommand;
import uo.sdi.business.impl.task.command.DuplicateCategoryCommand;
import uo.sdi.business.impl.task.command.MarkTaskAsFinishedCommand;
import uo.sdi.business.impl.task.command.UpdateCategoryCommand;
import uo.sdi.business.impl.task.command.UpdateTaskCommand;
import uo.sdi.business.integration.GTDSender;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.persistence.Persistence;

/**
 * Session Bean implementation class EjbTaskService
 */
@Stateless
// @LocalBean
@WebService(name = "TaskService")
public class EjbTaskService implements RemoteTaskService, LocalTaskService {

	@EJB
	private GTDSender sender;

	@Override
	public Long createCategory(Category category) throws BusinessException {
		if (sender != null)
			sender.send("createCategory", null);
		return new CreateCategoryCommand(category).execute();
	}

	@Override
	public Long duplicateCategory(Long id) throws BusinessException {
		if (sender != null)
			sender.send("duplicateCategory", null);
		return new DuplicateCategoryCommand(id).execute();
	}

	@Override
	public void updateCategory(Category category) throws BusinessException {
		if (sender != null)
			sender.send("updateCategory", null);
		new UpdateCategoryCommand(category).execute();
	}

	@Override
	public void deleteCategory(Long catId) throws BusinessException {
		sender.send("deleteCategory", null);
		new DeleteCategoryCommand(catId).execute();
	}

	@Override
	public Category findCategoryById(final Long id) throws BusinessException {
		if (sender != null)
			sender.send("findCategoryById", null);
		return new Command<Category>() {
			@Override
			public Category execute() throws BusinessException {

				return Persistence.getCategoryDao().findById(id);
			}
		}.execute();
	}

	@Override
	public List<Category> findCategoriesByUserId(final Long id)
			throws BusinessException {
		if (sender != null)
			sender.send("findCategoriesByUserId", null);
		return new Command<List<Category>>() {
			@Override
			public List<Category> execute() throws BusinessException {

				return Persistence.getCategoryDao().findByUserId(id);
			}
		}.execute();
	}

	@Override
	public Long createTask(Task task) throws BusinessException {
		if (sender != null)
			sender.send("createTask", null);
		return new CreateTaskCommand(task).execute();
	}

	@Override
	public void deleteTask(final Long id) throws BusinessException {
		if (sender != null)
			sender.send("deleteTask", null);
		new Command<Void>() {
			@Override
			public Void execute() throws BusinessException {
				Persistence.getTaskDao().delete(id);
				return null;
			}
		}.execute();
	}

	@Override
	public void markTaskAsFinished(Long id) throws BusinessException {
		if (sender != null)
			sender.send("markTaskAsFinished", null);
		new MarkTaskAsFinishedCommand(id).execute();
	}

	@Override
	public void updateTask(Task task) throws BusinessException {
		if (sender != null)
			sender.send("updateTask", null);
		new UpdateTaskCommand(task).execute();
	}

	@Override
	public Task findTaskById(final Long id) throws BusinessException {
		if (sender != null)
			sender.send("findTaskById", null);
		return new Command<Task>() {
			@Override
			public Task execute() throws BusinessException {

				return Persistence.getTaskDao().findById(id);
			}
		}.execute();
	}

	@Override
	public List<Task> findInboxTasksByUserId(final Long id)
			throws BusinessException {
		if (sender != null)
			sender.send("findInboxTasksByUserId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findInboxTasksByUserId(id);
			}
		}.execute();
	}

	@Override
	public List<Task> findWeekTasksByUserId(final Long id)
			throws BusinessException {
		if (sender != null)
			sender.send("findWeekTasksByUserId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findWeekTasksByUserId(id);
			}
		}.execute();
	}

	@Override
	public List<Task> findTodayTasksByUserId(final Long id)
			throws BusinessException {
		List<Task> todayTask = new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findTodayTasksByUserId(id);
			}
		}.execute();
		if (sender != null)
			sender.send("findTodayTasksByUserId", todayTask);
		return todayTask;
	}

	@Override
	public List<Task> findTasksByCategoryId(final Long id)
			throws BusinessException {
		if (sender != null)
			sender.send("findTasksByCategoryId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findTasksByCategoryId(id);
			}
		}.execute();
	}

	@Override
	public List<Task> findFinishedTasksByCategoryId(final Long id)
			throws BusinessException {
		if (sender != null)
			sender.send("findFinishedTasksByCategoryId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findFinishedTasksByCategoryId(
						id);
			}
		}.execute();
	}

	@Override
	public List<Task> findTaskFinishedPendingByCategoryId(final Long categoryId)
			throws BusinessException {
		if (sender != null)
			sender.send("findTaskFinishedPendingByCategoryId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {
				return Persistence.getTaskDao()
						.findTaskFinishedPendingByCategoryId(categoryId);
			}
		}.execute();
	}

	@Override
	public List<Task> findFinishedInboxTasksByUserId(final Long id)
			throws BusinessException {
		if (sender != null)
			if (sender != null)
				sender.send("findFinishedInboxTasksByUserId", null);
		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findFinishedTasksInboxByUserId(
						id);
			}
		}.execute();
	}

	@Override
	public List<Task> findCreatedNotFinished(final Long userId)
			throws BusinessException {
		if (sender != null)
			sender.send("findCreatedNotFinished", null);

		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao().findCreatedNotFinished(userId);
			}
		}.execute();
	}

	@Override
	public List<Task> findInboxNotFinishedTasksByUserId(final Long userId)
			throws BusinessException {
		if (sender != null)
			sender.send("findInboxNotFinishedTasksByUserId", null);

		return new Command<List<Task>>() {
			@Override
			public List<Task> execute() throws BusinessException {

				return Persistence.getTaskDao()
						.findInboxNotFinishedTasksByUserId(userId);
			}
		}.execute();
	}

	/**
	 * Default constructor.
	 */
	public EjbTaskService() {
		// TODO Auto-generated constructor stub
	}

}
