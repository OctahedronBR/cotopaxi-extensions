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

import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.model.response.ActionResponse;
import br.octahedron.cotopaxi.model.response.ActionResponse.Result;

/**
 * A filter that manage a {@link Transaction} for model operation.
 * 
 * It begins a {@link Transaction} when a request is received and commits the {@link Transaction} on
 * the end, rolling back if necessary.
 * 
 * The transaction is only commit if the request {@link ActionResponse} be an {@link Result#SUCCESS}
 * in other cases, the it rollback the transaction.
 * 
 * Its necessary that to set the {@link DatastoreFacade#detachObjectsOnQuery} to <code>false</code>;
 * default is <code>true</code>.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class TransactionFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger(TransactionFilter.class.getName());

	private PersistenceManagerPool pmp = PersistenceManagerPool.getInstance();

	@Override
	public void doAfter(RequestWrapper requestWrapper, ActionResponse response) throws FilterException {
		PersistenceManager pm = this.pmp.getPersistenceManagerForThread();
		Transaction tnx = pm.currentTransaction();
		switch (response.getResult()) {
		case SUCCESS:
			// if result is success, commit it
			try {
				logger.fine("Action Succeed; commit transaction");
				tnx.commit();
			} finally {
				if (tnx.isActive()) {
					logger.warning("Transaction still active after commit; rollback transaction.");
					tnx.rollback();
				}
			}
			break;
		default:
			// if not success, rollback
			logger.info("Action NOT Succeed; transaction rollback");
			tnx.rollback();
			break;
		}
	}

	@Override
	public void doBefore(RequestWrapper requestWrapper) throws FilterException {
		PersistenceManager pm = this.pmp.getPersistenceManagerForThread();
		Transaction tnx = pm.currentTransaction();
		logger.fine("Transaction begin");
		tnx.begin();
	}
}
