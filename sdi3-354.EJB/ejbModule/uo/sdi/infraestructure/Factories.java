package uo.sdi.infraestructure;

import uo.sdi.business.Services;
import uo.sdi.persistence.Persistence;

public class Factories {
	private static String CONFIG_FILE = "/factories.properties";

	public static Services services = (Services) FactoriesHelper.createFactory(
			CONFIG_FILE, "SERVICES_FACTORY");

	public static Persistence persistence = (Persistence) FactoriesHelper
			.createFactory(CONFIG_FILE, "PERSISTENCE_FACTORY");
}
