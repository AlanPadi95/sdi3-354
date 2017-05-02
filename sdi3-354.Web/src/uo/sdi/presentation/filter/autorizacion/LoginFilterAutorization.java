package uo.sdi.presentation.filter.autorizacion;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LoginFilterAutorization
 */

@WebFilter(dispatcherTypes = { DispatcherType.REQUEST },

urlPatterns = { "/privateAdmin/*", "/privateUser/*" }, initParams = { @WebInitParam(name = "LoginParam", value = "/login.xhtml") })
public class LoginFilterAutorization implements Filter {

	// Necesitamos acceder a los parámetros de inicialización en //el método
	// doFilter por lo que necesitamos la variable //config que se inicializará
	// en init()
	FilterConfig config = null;

	/**
	 * Default constructor.
	 */
	public LoginFilterAutorization() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {

	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// Si no es petición HTTP nada que hacer
		if (!(request instanceof HttpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}
		// En el resto de casos se verifica que se haya hecho login previamente
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		if (session.getAttribute("usuario") == null) {
			String loginForm = config.getInitParameter("LoginParam");
			// Si no hay login, redirección al formulario de login
			res.sendRedirect(req.getContextPath() + loginForm);
			return;
		}
		chain.doFilter(request, response);
	}

	/** * @see Filter#init(FilterConfig) */
	public void init(FilterConfig fConfig) throws ServletException {
		// Iniciamos la variable de instancia config
		config = fConfig;
	}

}