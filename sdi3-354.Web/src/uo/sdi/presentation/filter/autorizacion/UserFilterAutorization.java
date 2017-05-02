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

import uo.sdi.dto.User;

/**
 * Servlet Filter implementation class UserFilterAutorization
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, urlPatterns = { "/privateUser/*" }, initParams = { @WebInitParam(name = "UserParam", value = "/privateAdmin/userIndex.xhtml") })
public class UserFilterAutorization implements Filter {
	FilterConfig config = null;

	/**
	 * Default constructor.
	 */
	public UserFilterAutorization() {
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
		User user = (User) session.getAttribute("usuario");
		if (user == null) {
			res.sendRedirect(req.getContextPath() + "/login.xhtml");
		} else if (!user.getIsAdmin()) {
			String loginForm = config.getInitParameter("UserParam");
			// Si no hay login, redirección al formulario de login
			res.sendRedirect(req.getContextPath() + loginForm);
			return;
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.config = fConfig;
	}

}
