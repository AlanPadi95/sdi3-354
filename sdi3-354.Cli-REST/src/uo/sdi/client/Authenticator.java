package uo.sdi.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.jboss.resteasy.util.Base64;

public class Authenticator implements ClientRequestFilter {
	private final String user;
	private final String password;

	public Authenticator(String user, String password) {
		this.user = user;
		this.password = password;
	}

	@Override
	public void filter(ClientRequestContext ctx) throws IOException {
		MultivaluedMap<String, Object> headers = ctx.getHeaders();
		final String basicAuthentication = getBasicAuthentication();
		headers.add("Authorization", basicAuthentication);
	}

	private String getBasicAuthentication() {
		String token = this.user + ":" + this.password;
		try {
			// return "Basic "
			// + DatatypeConverter.printBase64Binary(token
			// .getBytes("UTF-8"));
			return Base64.encodeBytes(token.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode with UTF-8", ex);
		}
	}

	public String getUserername() {
		return user;
	}

	public String getPassword() {
		return password;
	}
}