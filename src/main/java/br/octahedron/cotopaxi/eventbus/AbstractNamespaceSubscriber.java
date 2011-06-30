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

import static br.octahedron.cotopaxi.datastore.NamespaceManagerFacade.changeToNamespace;
import static br.octahedron.cotopaxi.datastore.NamespaceManagerFacade.changeToPreviousNamespace;

/**
 * A subscriber that deals with different namespaces, when receiving NamespaceEvents.
 * 
 * @author Danilo Penna Queiroz
 */
public abstract class AbstractNamespaceSubscriber implements Subscriber {
	
	/* (non-Javadoc)
	 * @see br.octahedron.commons.eventbus.Subscriber#eventPublished(br.octahedron.commons.eventbus.Event)
	 */
	@Override
	public final void eventPublished(Event event) {
		try {
			if (event instanceof NamespaceEvent) {
				changeToNamespace(((NamespaceEvent)event).getNamespace());
			}
			this.processEvent(event);
		} finally {
			changeToPreviousNamespace();
		}
	}

	/**
	 * @see Subscriber#eventPublished(Event)
	 */
	protected abstract void processEvent(Event event);


}
