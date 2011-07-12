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
package br.octahedron.cotopaxi.datastore;

import java.lang.annotation.Annotation;

import br.octahedron.cotopaxi.inject.Inject;

/**
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 *
 */
public class NamespaceManagerInterceptor extends AbstractNamespaceManagerInterceptor {

	@Inject
	private NamespaceManager namespaceManager;
	
	protected void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.ControllerInterceptor#execute(java.lang.annotation.Annotation)
	 */
	@Override
	public void execute(Annotation ann) {
		if (ann instanceof NamespaceRequired) {
			NamespaceRequired namespace = (NamespaceRequired) ann;
			if (namespace.global()) {
				namespaceManager.changeToGlobalNamespace();
			} else {
				namespaceManager.changeToNamespace(subDomain());
			}
		}
	}

}
