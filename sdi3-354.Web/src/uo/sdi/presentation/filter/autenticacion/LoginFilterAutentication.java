package uo.sdi.presentation.filter.autenticacion;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.util.Base64;

import uo.sdi.business.Services;
import uo.sdi.business.UserService;
import uo.sdi.business.exception.BusinessException;

/**
 * Servlet Filter implementation class LoginFilterAutentication
 */
@WebFilter(urlPatterns = { "/rest/*" })
public class LoginFilterAutentication implements Filter {

	UserService login = new Services().getUserService();

	/**
	 * Default constructor.
	 */
	public LoginFilterAutentication() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (req.getHeader("Authorization") != null) {
			String cabecera = new String(Base64.decode(req
					.getHeader("Authorization")));

			String[] datos = cabecera.split(":");
			String username = datos[0];// Obtenemos el usuario
			String password = datos[1];// y la contraseña
			try {
				login.findLoggableUser(username, password);
			} catch (BusinessException e) {
				res.setStatus(401);
				e.printStackTrace();
			}// y verificamos al usuario
			chain.doFilter(request, response);
		}
		// return;

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
