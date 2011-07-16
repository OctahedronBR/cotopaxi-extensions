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
package br.octahedron.cotopaxi.datastore.jdo;

import javax.jdo.PersistenceManager;

import br.octahedron.cotopaxi.interceptor.ResponseDispatcherInterceptor;

/**
 * A {@link ResponseDispatcherInterceptor} for close opened {@link PersistenceManager}.
 * 
 * @author Danilo Queiroz
 */
public class PersistenceManagerInterceptor extends ResponseDispatcherInterceptor {

	private PersistenceManagerPool pmp = PersistenceManagerPool.getInstance();

	/* (non-Javadoc)
	 * @see br.octahedron.cotopaxi.interceptor.ResponseDispatcherInterceptor#finish()
	 */
	@Override
	public void finish() {
		if (this.pmp.isPersistenceManagerOpened()) {
			this.pmp.close();
		}
	}
}
