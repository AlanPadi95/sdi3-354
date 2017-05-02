package uo.sdi.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;

@Path("TaskServiceRest")
public interface TaskServicesRest {

	@GET
	@Path("userId/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	List<Category> findCategoriesByUserId(@PathParam("id") Long id)
			throws BusinessException;

	@GET
	@Path("categoryId/{categoryId}/userId/{userId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	List<Task> findTaskByCategoryId(@PathParam("categoryId") Long categoryId,
			@PathParam("userId") Long userId) throws BusinessException;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	void markAsFinished(Long taskId, Long userId) throws BusinessException;

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	void addTask(Task task) throws BusinessException;

}
