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

import static br.octahedron.cotopaxi.auth.AuthorizationRequired.CONTROLLER_NAME;

import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;

/**
 * An abstract interceptor to be used to create authorization interceptors.
 * 
 * @see ControllerInterceptor
 * 
 * @author Danilo Queiroz - daniloqueiro@octahedron.com.br
 */
public abstract class AbstractAuthorizationInterceptor extends ControllerInterceptor {

	@Override
	public final void execute(Annotation ann) {
		AuthorizationRequired auth = (AuthorizationRequired) ann;
		if (!this.isAnswered()) {
			String action = auth.actionName();
			if (CONTROLLER_NAME.equals(action)) {
				action = this.controllerDescriptor().getControllerName();
			}
			this.authorizeUser(action, this.currentUser(), auth.showForbiddenPage());
		}
	}

	@Override
	public final Class<? extends Annotation> getInterceptorAnnotation() {
		return AuthorizationRequired.class;
	}

	/**
	 * This method should check if the current logged user, is a authorized to perform the given
	 * action.
	 * 
	 * The action is the {@link AuthorizationRequired#actionName()}, if defined, or the controller
	 * name.
	 * 
	 * If the user is authorized, you should mark the request as authorized using the method
	 * <code>authorized()</code>
	 * 
	 * @param actionName
	 *            the action being executed
	 * @param currentUser
	 *            The current user's name, or <code>null</code> if no current user set.
	 * @param showForbiddenPage If should show the forbidden page, or continue the processing as not authorized request
	 */
	protected abstract void authorizeUser(String actionName, String currentUser, boolean showForbiddenPage);
}
