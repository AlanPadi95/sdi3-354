package uo.sdi.dto;

import java.io.Serializable;

public class UserTask implements Serializable {

	private static final long serialVersionUID = 1L;
	private String username;
	private String email;
	private String status;
	private boolean isAdmin;
	private int tareasCompletadas;
	private int tareasCompletadasRetrasadas;
	private int tareasPlanificadas;
	private int tareasSinPlanificar;

	public int getTareasCompletadas() {
		return tareasCompletadas;
	}

	public void setTareasCompletadas(int tareasCompletadas) {
		this.tareasCompletadas = tareasCompletadas;
	}

	public int getTareasCompletadasRetrasadas() {
		return tareasCompletadasRetrasadas;
	}

	public void setTareasCompletadasRetrasadas(int tareasCompletadasRetrasadas) {
		this.tareasCompletadasRetrasadas = tareasCompletadasRetrasadas;
	}

	public int getTareasPlanificadas() {
		return tareasPlanificadas;
	}

	public void setTareasPlanificadas(int tareasPlanificadas) {
		this.tareasPlanificadas = tareasPlanificadas;
	}

	public int getTareasSinPlanificar() {
		return tareasSinPlanificar;
	}

	public void setTareasSinPlanificar(int tareasSinPlanificar) {
		this.tareasSinPlanificar = tareasSinPlanificar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}
