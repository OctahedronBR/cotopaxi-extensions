/*
 *  This file is part of Cotopaxi.
 *
 *  Cotopaxi is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Cotopaxi is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  Lesser GNU General Public License for more details.
 *
 *  You should have received a copy of the Lesser GNU General Public License
 *  along with Cotopaxi. If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.cotopaxi.auth;

import br.octahedron.util.Log;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * A abstract Authentication Interceptor that authenticate users using google authentication
 * mechanism.
 * 
 * If the user is logged, it adds the user email to session with CURRENT_USER_EMAIL key.
 * 
 * @see AbstractAuthenticationInterceptor
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class AbstractGoogleAuthenticationInterceptor extends AbstractAuthenticationInterceptor {


	private final UserService userService = UserServiceFactory.getUserService();
	private String authDomain;
	private String redirectUrl;
	protected Log log;
	
	/**
	 * Creates a new {@link AbstractAuthenticationInterceptor} that will authenticate the users
	 * using google authentication and the given auth domain, and will, after authenticate, redirect
	 * users to last called url.
	 * 
	 * @param authDomain
	 *            authentication domain to use.
	 */
	public AbstractGoogleAuthenticationInterceptor(Log log, String authDomain) {
		this(log, authDomain, null);
	}

	/**
	 * * Creates a new {@link AbstractAuthenticationInterceptor} that will authenticate the users
	 * using google authentication and the given auth domain, and will, after authenticate, redirect
	 * users the given url.
	 * 
	 * @param authDomain
	 *            authentication domain to use.
	 * @param redirectUrl
	 *            the redirect url to be used to redirect user after login.
	 */
	public AbstractGoogleAuthenticationInterceptor(Log log, String authDomain, String redirectUrl) {
		this.log = log;
		this.authDomain = authDomain;
		this.redirectUrl = redirectUrl;
	}

	@Override
	protected void checkUserAuthentication() {
		if (!this.isUserLogged()) {
			if (this.userService.isUserLoggedIn()) {
				log.debug("User is logged in GoogleAccounts");
				User user = this.userService.getCurrentUser();
				this.setUserLogged(user.getEmail());
			} else {
				log.debug("User is not logger, redirecting");
				this.redirectUnauthenticatedUser();
			}
		}
	}

	/**
	 * Redirects an Unauthenticated user to login page
	 */
	protected void redirectUnauthenticatedUser() {
		String dest;
		if (this.redirectUrl == null) {
			dest = this.userService.createLoginURL(this.fullRequestedUrl(), this.authDomain);
		} else {
			dest = this.userService.createLoginURL(this.redirectUrl, this.authDomain);
		}
		this.redirect(dest);
	}
}
