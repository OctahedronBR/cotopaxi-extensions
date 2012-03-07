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
package br.octahedron.cotopaxi.datastore.namespace;

import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.inject.Inject;
import br.octahedron.cotopaxi.interceptor.ControllerInterceptor;

/**
 * The base class for Namespace interceptors. Children classes should only define the strategy to be
 * used to changes the namespace.
 * 
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 */
public abstract class AbstractNamespaceInterceptor extends ControllerInterceptor {

	@Inject
	protected NamespaceManager namespaceManager;

	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}

	@Override
	public final Class<? extends Annotation> getInterceptorAnnotation() {
		return NamespaceRequired.class;
	}

	@Override
	public void execute(Annotation ann) {
		if (ann instanceof NamespaceRequired) {
			NamespaceRequired namespace = (NamespaceRequired) ann;
			if (namespace.global()) {
				this.namespaceManager.changeToGlobalNamespace();
			} else {
				this.changeNamespace();
			}
		}
	}

	/**
	 * Changes the namespace.
	 * 
	 * Children classes should override this method to define the strategy to be used to changes the
	 * namespace. There's no need to handle changes to the global namespace, it's handled by the
	 * this {@link AbstractNamespaceInterceptor}.
	 */
	public abstract void changeNamespace();
}
