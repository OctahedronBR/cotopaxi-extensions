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
package br.octahedron.cotopaxi.subdomain;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.octahedron.cotopaxi.controller.Controller;
import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;
import br.octahedron.util.Log;

/**
 * This interceptor only allows the controller to be executed if the subdomain is equals to 'www'.
 * If it isn't, it fires a not found.
 * 
 * To use it just added the {@link OnlyForGlobal} annotation to {@link Controller} class/method
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class OnlyForGlobalSubdomainControllerInterceptor extends ControllerInterceptor {
	
	private static final Log log = new Log(OnlyForGlobalSubdomainControllerInterceptor.class); 

	@Retention(value = RetentionPolicy.RUNTIME)
	@Target(value = {ElementType.METHOD, ElementType.TYPE})
	public @interface OnlyForGlobal {
	}

	@Override
	public void execute(Annotation arg0) {
		if (!this.subDomain().equalsIgnoreCase("www")) {
			log.debug("Address valid only for www");
			this.notFound();
		}

	}

	@Override
	public Class<? extends Annotation> getInterceptorAnnotation() {
		return OnlyForGlobal.class;
	}

}
