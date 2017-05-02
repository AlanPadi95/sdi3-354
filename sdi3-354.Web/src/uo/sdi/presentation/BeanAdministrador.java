package uo.sdi.presentation;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import uo.sdi.business.Services;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;
import uo.sdi.dto.util.ResetBD;
import uo.sdi.presentation.BeanLogin;

@ManagedBean(name = "controller")
@SessionScoped
public class BeanAdministrador implements Serializable {
	private static final long serialVersionUID = 55555L;
	// uso de inyección de dependencia
	@ManagedProperty(value = "#{login}")
	private BeanLogin usuario;
	private User selectedUser;

	private List<User> usuarios = null;

	public BeanAdministrador() {
		listUsers();
	}

	public BeanLogin getUsuario() {
		return usuario;
	}

	public void setUsuario(BeanLogin usuario) {
		this.usuario = usuario;
	}

	public List<User> getUsuarios() {
		return (usuarios);
	}

	public void setUsuarios(List<User> usuarios) {
		this.usuarios = usuarios;
	}

	public String changeStatus(User user) {
		try {
			if (user.getStatus().equals(UserStatus.DISABLED)) {
				Services s = new Services();
				s.getAdminService().enableUser(user.getId());
				System.out.println("DEBUG: desbloqueando el usuario "
						+ user.getLogin());
				listUsers();
				return "exito";
			} else {
				Services s = new Services();
				s.getAdminService().disableUser(user.getId());
				System.out.println("DEBUG: bloqueando el usuario "
						+ user.getLogin());
				listUsers();
				return "exito";
			}
		} catch (Exception e) {
			e.getMessage();
			return "error";
		}
	}

	public String listUsers() {
		try {
			Services s = new Services();
			List<User> aux = s.getAdminService().findAllUsers();
			List<User> aux2 = s.getAdminService().findAllUsers();

			for (User u : aux) {
				if (u.getIsAdmin())
					aux2.remove(u);
			}
			usuarios = aux2;
			System.out.println("DEBUG: Se ha cargado la lista de usuarios");
			return "exito";

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR: Se ha producido una excepción");
			return "error";
		}
	}

	public String bajaUsuario() {
		try {
			// Acceso a la implementacion de la capa de negocio
			// a trav��s de la factor��a
			// Aliminamos el alumno seleccionado en la tabla
			Services s = new Services();
			s.getAdminService().deepDeleteUser(selectedUser.getId());
			// Actualizamos el javabean de alumnos inyectado en la
			// tabla.
			listUsers();
			System.out.println("DEBUG: Se ha eliminado el usuario "
					+ selectedUser.getLogin());
			return "exito";

		} catch (Exception e) {
			e.printStackTrace();
			return "error"; // Nos vamos a la vista de error
		}
	}

	public String resetBD() {
		if (new ResetBD().iniciarBD().equals("exito")) {
			listUsers();
			System.out.println("DEBUG: Se ha reseteado la base de datos");
			return "exito";
		} else {
			System.err
					.println("ERROR: Se ha producido un error al resetear la base de datos");
			return "error";
		}
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}
}
