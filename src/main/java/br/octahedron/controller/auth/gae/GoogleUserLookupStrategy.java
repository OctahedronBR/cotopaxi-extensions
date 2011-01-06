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
package br.octahedron.controller.auth.gae;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import br.octahedron.cotopaxi.controller.auth.UserInfo;
import br.octahedron.cotopaxi.controller.auth.UserLookupStrategy;

/**
 * @author Name - email@octahedron.com.br
 *
 */
public class GoogleUserLookupStrategy implements UserLookupStrategy {
	
	private UserService userService = UserServiceFactory.getUserService();

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.controller.auth.UserLookupStrategy#getCurrentUSer()
	 */
	@Override
	public UserInfo getCurrentUSer() {
		UserInfo userInfo = null;
		if (this.userService.isUserLoggedIn()) {
			User user = this.userService.getCurrentUser();
			String username = user.getEmail();
			if (this.userService.isUserAdmin()) {
				userInfo = new UserInfo(username, "admin");
			} else {
				userInfo = new UserInfo(username, "user");
			}
		}
		return userInfo;
	}

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.controller.auth.UserLookupStrategy#getLoginURL(String)
	 */
	@Override
	public String getLoginURL(String redirectURL) {
		return this.userService.createLoginURL(redirectURL);
	}
}
