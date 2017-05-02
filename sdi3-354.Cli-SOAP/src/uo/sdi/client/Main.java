package uo.sdi.client;

import uo.sdi.client.action.DeepDeleteUserAction;
import uo.sdi.client.action.DisableUserAction;
import uo.sdi.client.action.ListUserAction;
import alb.util.menu.BaseMenu;

public class Main extends BaseMenu {
	public Main() {
		menuOptions = new Object[][] { { "Administrador", null },
				{ "Deshabilitar usuario", DisableUserAction.class },
				{ "Eliminar usuario", DeepDeleteUserAction.class },
				{ "Listar usuarios", ListUserAction.class } };
	}

	public static void main(String[] args) {
		new Main().execute();
	}

}
