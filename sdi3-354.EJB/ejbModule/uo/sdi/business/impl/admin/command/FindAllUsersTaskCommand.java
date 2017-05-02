package uo.sdi.business.impl.admin.command;

import java.util.ArrayList;
import java.util.List;

import uo.sdi.business.exception.BusinessException;
import uo.sdi.business.impl.command.Command;
import uo.sdi.dto.Task;
import uo.sdi.dto.User;
import uo.sdi.dto.UserTask;
import uo.sdi.persistence.Persistence;
import uo.sdi.persistence.TaskDao;
import uo.sdi.persistence.UserDao;
import alb.util.date.DateUtil;

public class FindAllUsersTaskCommand implements Command<List<UserTask>> {

	@Override
	public List<UserTask> execute() throws BusinessException {
		UserDao uDao = Persistence.getUserDao();
		TaskDao tDao = Persistence.getTaskDao();
		List<User> usuarios = uDao.findAll();
		List<UserTask> listUT = new ArrayList<UserTask>();

		for (User u : usuarios) {
			List<Task> tareas = tDao.findByUserId(u.getId());
			UserTask uT = new UserTask();
			uT.setUsername(u.getLogin());
			uT.setEmail(u.getEmail());
			uT.setStatus(u.getStatus().name());
			uT.setAdmin(u.getIsAdmin());
			countTareas(u, uT, tareas);
			listUT.add(uT);

		}
		return listUT;
	}

	/**
	 * Asigna los contadores a los atributos del User DTO
	 * 
	 * @param u
	 *            User DTO
	 * @param uT
	 * @param completadas
	 *            número de tareas completadas
	 * @param completadasRetrasadas
	 *            número de tareas completadas y retrasadas
	 * @param planificadas
	 *            número de tareas planificadas
	 * @param noPlanificadas
	 *            número de tareas sin planificar
	 */
	private void asignaContadores(User u, UserTask uT, int completadas,
			int completadasRetrasadas, int planificadas, int noPlanificadas) {
		uT.setTareasCompletadas(completadas);
		uT.setTareasCompletadasRetrasadas(completadasRetrasadas);
		uT.setTareasPlanificadas(planificadas);
		uT.setTareasSinPlanificar(noPlanificadas);
	}

	/**
	 * Cuenta el número de tareas de cada tipo que tiene el usuario y se las
	 * asigna a su User DTO
	 * 
	 * @param u
	 *            el User DTO
	 * @param uT
	 * @param tareas
	 *            la lista de tareas del usuario
	 */
	private void countTareas(User u, UserTask uT, List<Task> tareas) {
		int completadas = 0;
		int completadasRetrasadas = 0;
		int planificadas = 0;
		int noPlanificadas = 0;
		for (Task t : tareas) {
			if (t.getFinished() != null) {

				completadas++;

			}
			if (t.getFinished() != null
					&& DateUtil.isAfter(t.getFinished(), t.getPlanned())) {

				completadasRetrasadas++;
			}
			if (t.getPlanned() != null) {

				planificadas++;

			} else {
				noPlanificadas++;
			}

		}
		asignaContadores(u, uT, completadas, completadasRetrasadas,
				planificadas, noPlanificadas);
	}
}
