package uo.sdi.dto.util;

import java.util.ArrayList;
import java.util.List;

import uo.sdi.business.Services;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import alb.util.date.DateUtil;

public class ResetBD {

	private void createUsers() {
		try {
			for (int i = 1; i <= 3; i++) {
				User u = new User();
				u.setLogin("usuario" + i);
				u.setPassword("usuario" + i);
				u.setIsAdmin(false);
				u.setEmail("usuario" + i + "@correo.com");
				u.setStatus(UserStatus.ENABLED);
				Services s = new Services();
				s.getUserService().registerUser(u);
			}
		} catch (BusinessException e) {
			System.err
					.println("ERROR: se ha producido un error creando los usuarios");
			e.getMessage();
		}
	}

	private void createCategorias(User u) {
		for (int i = 1; i <= 3; i++) {
			try {
				Category c = new Category();
				c.setName("categoría" + i);
				c.setUserId(u.getId());
				Services s = new Services();
				s.getTaskService().createCategory(c);
			} catch (BusinessException e) {
				System.err
						.println("ERROR: se ha producido un error creando las categorías");
				e.getMessage();
			}
		}
	}

	private void createTareasSixDays(User u) {
		// Sin categorizar
		for (int i = 1; i <= 10; i++) {
			try {
				Task t = new Task();
				t.setTitle("Tarea " + i);
				t.setComments("Tarea " + i
						+ " prevista para los siguientes 6 días a hoy");
				t.setCreated(DateUtil.today());
				t.setPlanned(DateUtil.addDays(t.getCreated(), 6));
				t.setUserId(u.getId());
				Services s = new Services();
				s.getTaskService().createTask(t);
			} catch (BusinessException e) {
				System.err
						.println("ERROR: se ha producido un error creando las tareas previstas para los siguientes 6 díasa hoy");
				e.getMessage();
			}
		}
	}

	private void createTareasToday(User u) {
		// Sin categorizar
		for (int i = 11; i <= 20; i++) {
			try {
				Task t = new Task();
				t.setTitle("Tarea " + i);
				t.setComments("Tarea " + i
						+ " para el día que se ejecuta la tarea");
				t.setCreated(DateUtil.today());
				t.setPlanned(t.getCreated());
				t.setUserId(u.getId());
				Services s = new Services();
				s.getTaskService().createTask(t);
			} catch (BusinessException e) {
				System.err
						.println("ERROR: se ha producido un error creando las tareas para el día en que se ejecutan las tareas");
				e.getMessage();
			}
		}
	}

	private void createTareasConCategoría(User u, List<Category> categorias) {
		for (Category c : categorias) {
			if (c.getName().equals("categoría1")
					&& c.getUserId().equals(u.getId()))
				for (int i = 21; i <= 23; i++) {
					createTask(u, c, i);
				}
			else if (c.getName().equals("categoría2")
					&& c.getUserId().equals(u.getId()))
				for (int i = 24; i <= 26; i++) {
					createTask(u, c, i);
				}
			else if (c.getName().equals("categoría3")
					&& c.getUserId().equals(u.getId()))
				for (int i = 27; i <= 30; i++) {
					createTask(u, c, i);
				}

		}

	}

	private void createTask(User u, Category c, int i) {
		try {
			Task t = new Task();
			t.setTitle("Tarea " + i);
			t.setComments("Tarea "
					+ i
					+ "  retrasada respecto al día que ejecute la prueba y perteneciente a la categoría: "
					+ c.getName());
			t.setCreated(DateUtil.subDays(DateUtil.today(), 3));
			t.setPlanned(DateUtil.subDays(t.getCreated(), 1));
			t.setUserId(u.getId());
			t.setCategoryId(c.getId());
			Services s = new Services();
			s.getTaskService().createTask(t);
		} catch (BusinessException e) {
			System.err
					.println("ERROR: se ha producido un error creando las tareas retrasadas y categorizadas");
			e.getMessage();
		}
	}

	private void createTareas(User u, List<Category> categorias) {
		createTareasSixDays(u);
		createTareasToday(u);
		createTareasConCategoría(u, categorias);
	}

	public String iniciarBD() {
		deleteBD();
		createUsers();
		List<User> users = getUsersBD();
		if (users.isEmpty())
			return "error";
		for (User u : users) {
			createCategorias(u);
			List<Category> categorias = getCategorysBD(users);
			if (categorias.isEmpty())
				return "error";
			createTareas(u, categorias);
		}
		return "exito";

	}

	private List<Category> getCategorysBD(List<User> users) {
		List<Category> categorias = new ArrayList<Category>();
		try {
			for (User u : users) {
				Services s = new Services();
				List<Category> cS = s.getTaskService().findCategoriesByUserId(
						u.getId());
				for (Category c : cS) {
					categorias.add(c);
				}
			}
		} catch (BusinessException e) {
			e.getMessage();

		}
		return categorias;
	}

	private List<User> getUsersBD() {
		List<User> users = null;
		try {
			Services s = new Services();
			List<User> aux = s.getAdminService().findAllUsers();
			users = s.getAdminService().findAllUsers();
			for (User u : aux) {
				if (u.getIsAdmin())
					users.remove(u);
			}
			return users;
		} catch (BusinessException e) {
			e.getMessage();
		}

		return users;
	}

	private void deleteBD() {
		List<User> users;
		try {
			users = getUsersBD();
			for (User u : users) {
				Services s = new Services();
				s.getAdminService().deepDeleteUser(u.getId());
			}
		} catch (BusinessException e) {
			e.getMessage();
		}

	}
}
