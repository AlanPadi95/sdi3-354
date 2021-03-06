package uo.sdi.rest;

import java.util.HashSet;
import java.util.Set;

import uo.sdi.rest.impl.TaskServiceRestImpl;
import uo.sdi.rest.impl.UserServiceRestImpl;

public class Application extends javax.ws.rs.core.Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> res = new HashSet<>();
		res.add(UserServiceRestImpl.class);
		res.add(TaskServiceRestImpl.class);
		return res;
	}

}