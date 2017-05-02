package uo.sdi.rest.impl;

import java.util.List;

import uo.sdi.business.Services;
import uo.sdi.business.TaskService;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.rest.TaskServicesRest;

public class TaskServiceRestImpl implements TaskServicesRest {

	TaskService taskService = new Services().getTaskService();// Factories.services.getTaskService();

	@Override
	public List<Category> findCategoriesByUserId(Long id)
			throws BusinessException {
		List<Category> categorias = taskService.findCategoriesByUserId(id);
		return categorias;
	}

	@Override
	public List<Task> findTaskByCategoryId(Long categoryId, Long userId)
			throws BusinessException {
		Category cat = taskService.findCategoryById(categoryId);
		if (cat != null && cat.getUserId().equals(userId)) {
			List<Task> tareas = taskService
					.findTaskFinishedPendingByCategoryId(categoryId);
			return tareas;
		}
		return null;

	}

	@Override
	public void markAsFinished(Long taskId, Long userId)
			throws BusinessException {
		Task task = taskService.findTaskById(taskId);
		if (task != null && task.getUserId().equals(userId)) {
			taskService.markTaskAsFinished(taskId);
		}
	}

	@Override
	public void addTask(Task task) throws BusinessException {
		taskService.createTask(task);
	}

}
