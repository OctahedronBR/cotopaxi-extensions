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

import br.octahedron.cotopaxi.datastore.jdo.DatastoreFacade.PMFWrapper;
import br.octahedron.util.Log;

/**
 * @see PersistenceManagerPool
 * 
 * @author Danilo Penna Queiroz
 */
public class PersistenceManagerPool {

	private static final Log log = new Log(PersistenceManagerPool.class);
	private static final PersistenceManagerPool instance = new PersistenceManagerPool();

	protected static PersistenceManagerPool getInstance() {
		return instance;
	}

	/**
	 * Closes the current connection to database.
	 * 
	 * This method should be used carefully. Use this only when you are doing background tasks.
	 * After closes the connection any object retrieved from database become to a inconsistent
	 * state, and shouldn't be used (read or modified) anymore.
	 */
	public static void forceClose() {
		instance.close();
	}

	// Object stuff
	private ThreadLocal<PersistenceManager> pool = new ThreadLocal<PersistenceManager>();

	private PersistenceManagerPool() {
		// private constructor
	}

	protected boolean isPersistenceManagerOpened() {
		PersistenceManager pm = this.pool.get();
		return (pm != null) && !pm.isClosed();
	}

	protected void close() {
		if (instance.isPersistenceManagerOpened()) {
			log.debug("Closing PersistenceManager.");
			PersistenceManager pm = this.pool.get();
			pm.close();
			this.pool.remove();
		}
	}

	public PersistenceManager getPersistenceManagerForThread() {
		log.debug("Getting a PersistenceManager.");
		PersistenceManager pm = this.pool.get();
		if (pm == null || pm.isClosed()) {
			log.debug("Creating a new PersistenceManager.");
			pm = PMFWrapper.getPersistenceManager();
			this.pool.set(pm);
		}
		return pm;
	}
}
