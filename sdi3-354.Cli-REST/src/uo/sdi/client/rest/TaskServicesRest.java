package uo.sdi.client.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.model.Category;
import uo.sdi.model.Task;

@Path("TaskServiceRest")
public interface TaskServicesRest {

	@GET
	@Path("userId/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	List<Category> findCategoriesByUserId(@PathParam("id") Long id);

	@GET
	@Path("categoryId/{categoryId}/userId/{userId}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	List<Task> findTaskByCategoryId(@PathParam("categoryId") Long categoryId,
			@PathParam("userId") Long userId);

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	void markAsFinished(Long taskId, Long userId);

	@PUT
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	void addTask(Task task);

}
