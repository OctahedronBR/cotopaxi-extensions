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
package br.octahedron.cloudservice.gae.ds;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.model.response.ActionResponse;

/**
 * A filter that manage a {@link Transaction} for model operation.
 * 
 * It begins a {@link Transaction} when a request is received and commits the {@link Transaction} on
 * the end, rolling back if necessary.
 * 
 * Its necessary that to set the {@link DatastoreFacade#detachObjectsOnQuery} to <code>false</code>;
 * default is <code>true</code>.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class TransactionFilter implements Filter {

	private PersistenceManagerPool pmp = PersistenceManagerPool.getInstance();

	@Override
	public void doAfter(RequestWrapper requestWrapper, ActionResponse response) throws FilterException {
		PersistenceManager pm = this.pmp.getPersistenceManagerForThread();
		Transaction tnx = pm.currentTransaction();
		try {
			tnx.commit();
		} finally {
			if (tnx.isActive()) {
				tnx.rollback();
			}
		}
	}

	@Override
	public void doBefore(RequestWrapper requestWrapper) throws FilterException {
		PersistenceManager pm = this.pmp.getPersistenceManagerForThread();
		Transaction tnx = pm.currentTransaction();
		tnx.begin();
	}
}
