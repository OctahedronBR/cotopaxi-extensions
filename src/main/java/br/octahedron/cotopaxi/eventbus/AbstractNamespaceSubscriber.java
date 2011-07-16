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

import br.octahedron.cotopaxi.datastore.namespace.NamespaceManager;
import br.octahedron.cotopaxi.inject.Inject;

/**
 * A subscriber that deals with different namespaces, when receiving NamespaceEvents.
 * 
 * @author Danilo Penna Queiroz
 */
public abstract class AbstractNamespaceSubscriber implements Subscriber {
	
	@Inject
	private NamespaceManager namespaceManager;
	
	public void setNamespaceManager(NamespaceManager namespaceManager) {
		this.namespaceManager = namespaceManager;
	}
	
	/* (non-Javadoc)
	 * @see br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event)
	 */
	@Override
	public final void eventPublished(Event event) {
		try {
			if (event instanceof NamespaceEvent) {
				namespaceManager.changeToNamespace(((NamespaceEvent)event).getNamespace());
			}
			this.processEvent(event);
		} finally {
			namespaceManager.changeToPreviousNamespace();
		}
	}

	/**
	 * @see Subscriber#eventPublished(Event)
	 */
	protected abstract void processEvent(Event event);


}
