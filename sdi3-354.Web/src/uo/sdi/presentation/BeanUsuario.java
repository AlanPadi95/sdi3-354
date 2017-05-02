package uo.sdi.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import uo.sdi.business.Services;
import uo.sdi.business.exception.BusinessException;
import uo.sdi.dto.Category;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.presentation.BeanLogin;
import alb.util.date.DateUtil;

@ManagedBean(name = "user")
@SessionScoped
public class BeanUsuario {
	@ManagedProperty(value = "#{login}")
	private BeanLogin usuario;
	private Long id;
	private String name;
	private String comment;
	private Date createdDate;
	private Date plannedDate;
	private Date finishDate;
	private User userLogued;
	private List<Category> cS;
	private List<String> categories;
	private String category;
	private List<Task> tareas;
	private List<Task> filteredTask;
	private Boolean isInbox;
	private Boolean isHoy;
	private Boolean isSemana;
	private Boolean isNotFinished;
	@SuppressWarnings("unused")
	private Boolean isInboxOrHoy;
	@SuppressWarnings("unused")
	private Date today;

	public BeanUsuario() {
		setUserLogued(obtenerUsuario());
		setCategories(cargarCategorias());
		listInitialTask();

	}

	public User obtenerUsuario() {
		System.out.println("DEBUG: cargando el usuario en sesion");
		return (User) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("usuario");
	}

	public BeanLogin getUsuario() {
		return usuario;
	}

	public void setUsuario(BeanLogin usuario) {
		this.usuario = usuario;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date date) {
		this.createdDate = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String vaciarCampos() {
		setId(null);
		setName(null);
		setCategory(null);
		setComment(null);
		setCreatedDate(null);
		setPlannedDate(null);
		return "exito";
	}

	@SuppressWarnings("finally")
	public String editTarea() {
		Task task = null;
		try {
			Category categoria = buscarCategoria();
			task = new Task().setId(this.getId())
					.setComments(this.getComment()).setTitle(this.getName())
					.setCreated(DateUtil.today())
					.setPlanned(this.getPlannedDate())
					.setUserId(obtenerUsuario().getId());
			if (!categoria.equals(null))
				task.setCategoryId(categoria.getId());
		} catch (NullPointerException e) {
			System.out.println("DEBUG: El usuario "
					+ obtenerUsuario().getLogin() + " ha añadido la tarea "
					+ task.getTitle() + " sin categoría");
		} finally {
			try {
				Services s = new Services();
				s.getTaskService().updateTask(task);
				System.out.println("DEBUG: El usuario "
						+ obtenerUsuario().getLogin() + " ha editado la tarea "
						+ task.getTitle() + " con categoría " + category);
				vaciarCampos();
				listInitialTask();
				return "exito";
			} catch (final BusinessException e1) {
				System.err.println("ERROR: No se ha podido editar la tarea "
						+ task.getTitle());
				e1.printStackTrace();
				listInitialTask();
				vaciarCampos();
				return "error";
			}

		}
	}

	@SuppressWarnings("finally")
	public String addTarea() {
		Task task = null;
		Category categoria = null;
		try {
			categoria = buscarCategoria();
			task = new Task().setComments(this.getComment())
					.setTitle(this.getName()).setCreated(DateUtil.today())
					.setPlanned(this.getPlannedDate())
					.setUserId(obtenerUsuario().getId());
			if (!categoria.equals(null))
				task.setCategoryId(categoria.getId());
		} catch (NullPointerException e) {
			System.out.println("DEBUG: El usuario "
					+ obtenerUsuario().getLogin() + " ha añadido la tarea "
					+ task.getTitle() + " sin categoría");
		} finally {
			try {
				Services s = new Services();
				s.getTaskService().createTask(task);
				System.out.println("DEBUG: El usuario "
						+ obtenerUsuario().getLogin() + " ha añadido la tarea "
						+ task.getTitle() + " con categoría" + category);
				vaciarCampos();
				listInitialTask();
				return "exito";
			} catch (final BusinessException e1) {
				System.err.println("ERROR: No se ha podido añadir la tarea "
						+ task.getTitle() + "\n" + e1.getMessage());
				listInitialTask();
				vaciarCampos();
				return "error";
			}
		}

	}

	private Category buscarCategoria() {
		for (Category categoria : cS) {
			if (category.equals(categoria.getName()))
				return categoria;
		}

		return null;
	}

	public User getUserLogued() {
		return userLogued;
	}

	public void setUserLogued(User userLogued) {
		this.userLogued = obtenerUsuario();
	}

	public List<String> getCategories() {
		return categories;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> cargarCategorias() {
		List<String> se = new ArrayList<String>();
		try {
			Services s = new Services();
			this.cS = s.getTaskService().findCategoriesByUserId(
					this.userLogued.getId());
			for (Category cat : cS) {
				se.add(cat.getName());
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			return null;
		}

		return se;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<Category> getcS() {
		return cS;
	}

	public void setcS(List<Category> cS) {
		this.cS = cS;
	}

	public List<Task> getTareas() {
		return tareas;
	}

	public void setTareas(List<Task> tareas) {
		this.tareas = tareas;
	}

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public String saveTask(Task task) {

		if (task != null) {
			setId(task.getId());
			setName(task.getTitle());
			setComment(task.getComments());
			setCreatedDate(task.getCreated());
			setPlannedDate(task.getPlanned());
			setFinishDate(task.getFinished());
			setCategory(getTaskCategory(task.getCategoryId()));
			System.out
					.println("DEBUG: Se están guardando los datos de la tarea "
							+ task.getTitle());
			return "editar";
		} else {
			System.err.println("ERROR: No existe la tarea");
			return "error";
		}
	}

	public void markAsFinished(Task task) {
		try {
			Services s = new Services();
			s.getTaskService().markTaskAsFinished(task.getId());
			System.out.println("DEBUG: Se ha marcado como finalizada la tarea "
					+ task.getTitle());
			listInitialTask();
		} catch (BusinessException e) {
			System.err
					.println("ERROR: No se ha podido marcar como finalizada la tarea "
							+ task.getTitle());
			e.printStackTrace();
		}
	}

	public String getTaskCategory(Long id) {
		try {
			Services s = new Services();
			Category cat = s.getTaskService().findCategoryById(id);
			if (cat != null) {
				System.out.println("DEBUG: Obteniendo la categoría");
				return cat.getName();
			} else {
				System.out.println("DEBUG: No existe dicha categoría");
				return "";
			}

		} catch (BusinessException e) {
			System.err.println("ERROR: No se ha encontrado la categoría");
			e.printStackTrace();
		}
		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Task> getFilteredTask() {
		return filteredTask;
	}

	public void setFilteredTask(List<Task> filteredTask) {
		this.filteredTask = filteredTask;
	}

	public String listInitialTask() {
		try {
			System.out
					.println("DEBUG: cargando la lista de tareas no finalizadas");
			Services s = new Services();
			tareas = s.getTaskService().findCreatedNotFinished(
					getUserLogued().getId());
			setIsNotFinished(true);
			return "listar";
		} catch (BusinessException e) {
			System.err
					.println("ERROR: no se ha podido cargar la lista de tareas no finalizadas");
			e.printStackTrace();
		}
		return "error";
	}

	public String listInbox() {
		try {
			Services s = new Services();
			System.out.println("DEBUG: cargando la lista de tareas Inbox");
			setTareas(s.getTaskService().findInboxTasksByUserId(
					obtenerUsuario().getId()));
			setIsInbox(true);
			return "listar";

		} catch (BusinessException e) {
			System.err.println("ERROR: no se ha podido cargar la lista Inbox");
			e.printStackTrace();
		}
		return "error";
	}

	public String listInboxNotFinished() {
		try {
			System.out
					.println("DEBUG: cargando la lista de tareas no finalizadas Inbox");
			Services s = new Services();
			setTareas(s.getTaskService().findInboxNotFinishedTasksByUserId(
					obtenerUsuario().getId()));
			setIsInbox(true);
			return "listar";
		} catch (BusinessException e) {
			System.err
					.println("ERROR: no se ha podido cargar la lista no finalizadas Inbox");
			e.printStackTrace();
		}
		return "error";
	}

	public String listInboxFinished() {
		try {
			System.out
					.println("DEBUG: cargando la lista de tareas finalizadas Inbox");
			Services s = new Services();
			setTareas(s.getTaskService().findFinishedInboxTasksByUserId(
					obtenerUsuario().getId()));
			setIsInbox(true);
			return "listar";

		} catch (BusinessException e) {
			System.err
					.println("ERROR: no se ha podido cargar la lista finalizadas Inbox");
			e.printStackTrace();
		}
		return "error";
	}

	public String listHoy() {
		try {
			System.out.println("DEBUG: cargando la lista de tareas Hoy");
			Services s = new Services();
			setTareas(s.getTaskService().findTodayTasksByUserId(
					obtenerUsuario().getId()));
			setIsHoy(true);
			return "listar";

		} catch (BusinessException e) {
			System.err.println("ERROR: no se ha podido cargar la lista Hoy");
			e.printStackTrace();
		}
		return "error";
	}

	public String listSemana() {
		try {
			System.out.println("DEBUG: cargando la lista de tareas Semana");
			Services s = new Services();
			setTareas(s.getTaskService().findWeekTasksByUserId(
					obtenerUsuario().getId()));
			setIsSemana(true);
			return "listar";

		} catch (BusinessException e) {
			System.err.println("ERROR: no se ha podido cargar la lista Semana");
			e.printStackTrace();
		}
		return "error";
	}

	public Boolean getIsInbox() {
		return isInbox;
	}

	public void setIsInbox(Boolean isInbox) {
		this.isInbox = isInbox;
		if (isInbox) {
			setIsHoy(false);
			setIsSemana(false);
			setIsNotFinished(false);
		}
	}

	public Boolean getIsHoy() {
		return isHoy;
	}

	public void setIsHoy(Boolean isHoy) {
		this.isHoy = isHoy;
		if (isHoy) {
			setIsInbox(false);
			setIsSemana(false);
			setIsNotFinished(false);
		}
	}

	public Boolean getIsSemana() {
		return isSemana;

	}

	public void setIsSemana(Boolean isSemana) {
		this.isSemana = isSemana;
		if (isSemana) {
			setIsHoy(false);
			setIsInbox(false);
			setIsNotFinished(false);
		}
	}

	public Boolean getIsNotFinished() {
		return isNotFinished;
	}

	public void setIsNotFinished(Boolean isNotFinished) {
		this.isNotFinished = isNotFinished;
		if (isNotFinished) {
			setIsHoy(false);
			setIsSemana(false);
			setIsInbox(false);
		}
	}

	public Date getToday() {
		return DateUtil.today();
	}

	public void setToday(Date today) {
		this.today = DateUtil.today();
	}

	public Boolean getIsInboxOrHoy() {
		if (isInbox || isHoy)
			return true;
		else
			return false;
	}

	public void setIsInboxOrHoy(Boolean isInboxOrHoy) {
		this.isInboxOrHoy = isInboxOrHoy;
	}

}
