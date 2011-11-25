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
 * This annotation should be used to indicate that a controller method needs authentication.
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD, ElementType.TYPE})
public @interface AuthenticationRequired {
	
	public enum AuthenticationLevel {
		/**
		 * Indicates that the user should only be authenticated 
		 */
		AUTHENTICATE, 
		/**
		 * Indicates that the user should be authenticated and valid. 
		 * @see AbstractAuthenticationInterceptor#checkValidUser() 
		 */
		AUTHENTICATE_AND_VALID;
	}
	
	/**
	 * The requested {@link AuthenticationLevel}. Default: {@link AuthenticationLevel#AUTHENTICATE_AND_VALID}
	 * @return the {@link AuthenticationLevel} for the {@link Controller} method
	 */
	AuthenticationLevel authenticationLevel() default AuthenticationLevel.AUTHENTICATE_AND_VALID;
}
