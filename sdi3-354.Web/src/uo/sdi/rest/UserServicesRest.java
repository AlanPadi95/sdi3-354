package uo.sdi.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;

@Path("UserServiceRest")
public interface UserServicesRest {

	@GET
	@Path("{login}/{password}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	User verifyUser(@PathParam("login") String login,
			@PathParam("password") String password) throws BusinessException;
}
