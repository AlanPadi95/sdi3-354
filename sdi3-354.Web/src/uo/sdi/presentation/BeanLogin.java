package uo.sdi.presentation;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import uo.sdi.business.Services;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.User;
import uo.sdi.dto.types.UserStatus;

@ManagedBean(name = "login")
@SessionScoped
public class BeanLogin extends User implements Serializable {
	private static final long serialVersionUID = 55556L;
	private String username;
	private String pass;
	private String correo;
	private Boolean admin;
	private UserStatus estado;

	public BeanLogin() {
		// iniciaUsuario(null);
	}

	@PostConstruct
	public void init() {
		// Log.debug("BeanLogin no existía");
	}

	public void iniciaUsuario(ActionEvent event) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ResourceBundle bundle = facesContext.getApplication()
				.getResourceBundle(facesContext, "msgs");
		setId(null);
		setIsAdmin(Boolean.parseBoolean(bundle
				.getString("valorDefectoUserIsAdmin")));
		setStatus(UserStatus.valueOf(bundle.getString("valorDefectoStatus")));
	}

	public String cerrarSesion() {
		User user = (User) FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap().get("usuario");
		if (user != null) {
			FacesContext.getCurrentInstance().getExternalContext()
					.invalidateSession();
			FacesContext.getCurrentInstance().getExternalContext()
					.getSessionMap().remove("usuario");
			System.out.println("DEBUG: El usuario " + username
					+ " ha cerrado sesión");
			return "cerrarSesion";
		} else {
			System.out.println("ERROR: No existe un usuario en sesión");
			return "error";
		}
	}

	public String verifyUser() {
		try {
			Services s = new Services();
			User user = s.getUserService().findLoggableUser(username, pass);
			if (user != null) {
				putUserInSession(user);
				if (user.getIsAdmin()) {
					System.out
							.println("DEBUG: Entrando en la cuenta del administrador");
					return "admin";
				}
				System.out.println("DEBUG: Entrando en la cuenta del usuario: "
						+ username);
				return "exito";
			} else {
				System.out
						.println("DEBUG: No existe un usuario con dichas credenciales");
				this.setPass(null);
				FacesContext facesContext = FacesContext.getCurrentInstance();
				ResourceBundle rB = facesContext.getApplication()
						.getResourceBundle(facesContext, "msgs");
				facesContext.addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, rB
								.getString("alertError")
								+ " "
								+ rB.getString("errorMessageLogin"), null));
				return "";
			}

		} catch (BusinessException e) {
			e.printStackTrace();
			System.err.println("ERROR: " + e.getMessage());
			return "error";
		}
	}

	public String registrarse() {
		try {
			Services s = new Services();
			List<User> usuarios = s.getAdminService().findAllUsers();
			for (User u : usuarios) {
				if (u.getLogin().equals(this.getUsername())) {
					System.err
							.println("ERROR: Ya existe un usuario con dicho login");
					FacesContext facesContext = FacesContext
							.getCurrentInstance();
					ResourceBundle rB = facesContext.getApplication()
							.getResourceBundle(facesContext, "msgs");
					facesContext.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, rB
									.getString("alertError")
									+ " "
									+ rB.getString("errorMessageRegistro"),
									null));
					return "";
				}
			}
			User user = new User().setEmail(this.getCorreo())
					.setLogin(this.getUsername()).setPassword(this.getPass());
			s.getUserService().registerUser(user);
			putUserInSession(user);
			System.out.println("DEBUG: El usuario " + user.getLogin()
					+ " ha sido registrado");
			return "exito";

		} catch (BusinessException e) {
			System.out
					.println("ERROR: Se ha producido un error al registrar al usuario "
							+ this.getUsername() + " " + e.getMessage());
			return "error";
		}
	}

	private void putUserInSession(User user) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put("usuario", user);
	}

	public User getUserInSession() {
		return (User) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("usuario");

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public UserStatus getEstado() {
		return estado;
	}

	public void setEstado(UserStatus estado) {
		this.estado = estado;
	}

}