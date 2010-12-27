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

import br.octahedron.cotopaxi.RequestWrapper;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.cotopaxi.controller.filter.Filter;
import br.octahedron.cotopaxi.controller.filter.FilterException;
import br.octahedron.cotopaxi.model.response.ActionResponse;

/**
 * A filter that closes the {@link PersistenceManager} for the current request/thread, if exists.
 * 
 * It should be used as a Global filter when set the {@link DatastoreFacade#detachObjectsOnQuery} to
 * <code>false</code>; default is <code>true</code>.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class PersistenceManagerFilter implements Filter {

	private PersistenceManagerPool pmp = PersistenceManagerPool.getInstance();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.filter.Filter#doAfter(br.octahedron.cotopaxi.RequestWrapper
	 * , br.octahedron.cotopaxi.model.ActionResponse)
	 */
	@Override
	public void doAfter(RequestWrapper requestWrapper, ActionResponse response) throws FilterException {
		if (pmp.isPersistenceManagerOpened()) {
			pmp.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.cotopaxi.controller.filter.Filter#doBefore(br.octahedron.cotopaxi.RequestWrapper
	 * )
	 */
	@Override
	public void doBefore(RequestWrapper requestWrapper) throws FilterException {
		// do nothing
	}

}
