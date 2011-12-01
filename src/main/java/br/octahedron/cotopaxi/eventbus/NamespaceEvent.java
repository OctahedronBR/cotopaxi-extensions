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
package br.octahedron.cotopaxi.eventbus;

import static br.octahedron.cotopaxi.inject.Injector.getInstance;
import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;

/**
 * Namespace events are events that refers to a given namespace.
 * 
 * @author Danilo Queiroz
 */
public abstract class NamespaceEvent implements Event {

	private static final long serialVersionUID = 6843928544469411033L;
	private String namespace;

	/**
	 * Creates a namespace event to be processed at the current namespace
	 */
	public NamespaceEvent() {
		try {
			NamespaceManager namespaceManager = getInstance(NamespaceManager.class);
			this.namespace = namespaceManager.currentNamespace();
		} catch (InstantiationException ex) {
			throw new RuntimeException("NamespaceManager has no implementation defined.", ex);
		}
	}

	/**
	 * Creates a namespace event to be processed at the given namespace
	 * 
	 * @param namespace
	 *            the namespace to process event
	 */
	public NamespaceEvent(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the namespace fot this event
	 */
	public String getNamespace() {
		return namespace;
	}

}
