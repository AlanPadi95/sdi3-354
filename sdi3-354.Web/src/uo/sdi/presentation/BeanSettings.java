package uo.sdi.presentation;

import java.io.Serializable;
import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "settings")
@SessionScoped
public class BeanSettings implements Serializable {
	private static final long serialVersionUID = 2L;
	private static final Locale ENGLISH = new Locale("en");
	private static final Locale SPANISH = new Locale("es");
	private Locale locale = new Locale("es");

	// uso de inyección de dependencia
	@ManagedProperty(value = "#{usuario}")
	private BeanLogin usuario;

	public BeanLogin getUsuario() {
		return usuario;
	}

	public void setUsuario(BeanLogin alumno) {
		this.usuario = alumno;
	}

	public Locale getLocale() {
		// Aqui habria que cambiar algo de código para coger locale del
		// navegador
		// la primera vez que se accede a getLocale(), de momento dejamos como
		// idioma de partida “es”
		return (locale);
	}

	public void setSpanish(ActionEvent event) {
		locale = SPANISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			if (usuario != null)
				usuario.iniciaUsuario(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setEnglish(ActionEvent event) {
		locale = ENGLISH;
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			if (usuario != null)
				usuario.iniciaUsuario(null);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/*
	 * // Se inicia correctamente el Managed Bean inyectado si JSF lo hubiera //
	 * creado // y en caso contrario se crea. // (hay que tener en cuenta que es
	 * un Bean de sesión) // Se usa @PostConstruct, ya que en el contructor no
	 * se sabe todavía si // el MBean ya estaba construido y en @PostConstruct
	 * SI.
	 * 
	 * @PostConstruct public void init() {
	 * System.out.println("BeanSettings - PostConstruct"); // Buscamos el alumno
	 * en la sesión. Esto es un patrón factoría // claramente. usuario =
	 * 
	 * (BeanUsuario) FacesContext.getCurrentInstance().getExternalContext()
	 * .getSessionMap().get(new String("usuario"));
	 * 
	 * // si no existe lo creamos e inicializamos if (usuario == null) {
	 * System.out.println("BeanSettings - No existia"); usuario = new
	 * BeanUsuario(); FacesContext.getCurrentInstance().getExternalContext()
	 * .getSessionMap().put("usuario", usuario); } }
	 * 
	 * // Es sólo a modo de traza.
	 * 
	 * @PreDestroy public void end() {
	 * System.out.println("BeanSettings - PreDestroy"); }
	 */
}