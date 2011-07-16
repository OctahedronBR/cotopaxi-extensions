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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation should be used to indicate that a controller method needs <i>authentication</i>
 * and <i>authorization</i>.
 * 
 * To use this annotation both {@link AbstractAuthenticationInterceptor} and
 * {@link AbstractAuthorizationInterceptor} should have implementations.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface AuthorizationRequired {

	static final String CONTROLLER_NAME = "%%%controller_name%%%";
	
	/**
	 * The action name to be used to authorize. If not defined, it will use the controller name.
	 */
	String actionName() default CONTROLLER_NAME;
	
	boolean showForbiddenPage() default true;
}
