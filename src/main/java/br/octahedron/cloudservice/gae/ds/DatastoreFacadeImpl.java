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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOFatalUserException;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.datanucleus.store.appengine.query.JDOCursorHelper;

import br.octahedron.cloudservice.gae.ReadOnlyDatastoreException;
import br.octahedron.cotopaxi.cloudservice.DatastoreFacade;
import br.octahedron.util.ThreadProperties;

import com.google.appengine.api.datastore.Cursor;
import com.google.apphosting.api.ApiProxy.CapabilityDisabledException;

/**
 * Facade to GAE DataStore Service. Provides methods to save, load and query objects in low level.
 * 
 * @see DatastoreFacade
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class DatastoreFacadeImpl implements DatastoreFacade {

	/*
	 * Every datastore write operation is atomic, so it isn't necessary to use transactions for
	 * save/delete operations over an unique entity.
	 */

	private int maxSize = 0;
	private boolean saveCursor = false;
	private boolean detach = true;
	protected PersistenceManagerPool pool = PersistenceManagerPool.getInstance();

	@Override
	public void setQueriesMaxSize(int size) {
		this.maxSize = size;
	}

	@Override
	public int getMaxQueriesSize() {
		return this.maxSize;
	}

	@Override
	public void detachObjectsOnQuery(boolean detach) {
		this.detach = detach;
	}

	@Override
	public boolean detachObjectsOnQuery() {
		return this.detach;
	}

	@Override
	public boolean saveCursor() {
		return this.saveCursor;
	}

	@Override
	public void saveCursor(boolean saveCursor) {
		this.saveCursor = saveCursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#saveObject(java.lang.Object)
	 */
	public <T> void saveObject(T persistentObject) throws ReadOnlyDatastoreException {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			pm.makePersistent(persistentObject);
		} catch (CapabilityDisabledException e) {
			throw new ReadOnlyDatastoreException();
		} finally {
			this.close(pm);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#saveAllObjects(java.util.Collection)
	 */
	@Override
	public <T> void saveAllObjects(Collection<T> persistentObjects) throws ReadOnlyDatastoreException {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			pm.setDetachAllOnCommit(this.detach);
			tx.begin();
			pm.makePersistentAll(persistentObjects);
			tx.commit();
		} catch (CapabilityDisabledException e) {
			throw new ReadOnlyDatastoreException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			this.close(pm);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#deleteObject(java.lang.Object)
	 */
	public <T> void deleteObject(T persistentObject) throws ReadOnlyDatastoreException {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			pm.deletePersistent(persistentObject);
		} catch (CapabilityDisabledException e) {
			throw new ReadOnlyDatastoreException();
		} finally {
			this.close(pm);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#deleteAllObjects(java.util.Collection)
	 */
	@Override
	public <T> void deleteAllObjects(Collection<T> persistentObjects) throws ReadOnlyDatastoreException {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.deletePersistentAll(persistentObjects);
			tx.commit();
		} catch (CapabilityDisabledException e) {
			throw new ReadOnlyDatastoreException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			this.close(pm);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#deleteObjects(java.lang.Class)
	 */
	@Override
	public <T> void deleteObjects(Class<T> klass) throws ReadOnlyDatastoreException {
		this.deleteObjectsByQuery(klass, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#deleteObjectsByQuery(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> void deleteObjectsByQuery(Class<T> klass, String filter) throws ReadOnlyDatastoreException {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Query query = this.prepareQuery(klass, filter, null, pm);
			query.deletePersistentAll();
			tx.commit();
		} catch (CapabilityDisabledException e) {
			throw new ReadOnlyDatastoreException();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			this.close(pm);
		}
	}

	@Override
	public <T> boolean existsObject(Class<T> klass, Object key) {
		if (key != null) {
			PersistenceManager pm = this.pool.getPersistenceManagerForThread();
			try {
				pm.getObjectById(klass, key);
				return true;
			} catch (JDOObjectNotFoundException ex) {
				// object not found, returning null
				return false;
			} finally {
				this.close(pm);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#getObjectByKey(java.lang.Class, java.lang.String)
	 */
	public <T> T getObjectByKey(Class<T> klass, Object key) {
		if (key != null) {
			PersistenceManager pm = this.pool.getPersistenceManagerForThread();
			try {
				T obj = pm.getObjectById(klass, key);
				if (this.detach) {
					return pm.detachCopy(obj);
				} else {
					return obj;
				}
			} catch (JDOObjectNotFoundException ex) {
				// object not found, returning null
				return null;
			} finally {
				this.close(pm);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#getObjects(java.lang.Class)
	 */
	public <T> Collection<T> getObjects(Class<T> klass) {
		return this.getObjectsByQuery(klass, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#getObjectsByQuery(java.lang.Class, java.lang.String)
	 */
	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter) {
		return this.getObjectsByQuery(klass, filter, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DatastoreFacade#getObjectsByQuery(java.lang.Class, java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public <T> Collection<T> getObjectsByQuery(Class<T> klass, String filter, String orderingAtts) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			Query query = this.prepareQuery(klass, filter, orderingAtts, pm);
			this.adjustCursor(query);
			List<T> objs = null;
			try {
				objs = (List<T>) query.execute();
			} catch (JDOFatalUserException e) {
				// if occurs any error using cursor (invalid cursor)
				query = this.prepareQuery(klass, filter, orderingAtts, pm);
				objs = (List<T>) query.execute();
			}
			this.saveCursor(objs);
			if (this.detach) {
				return pm.detachCopyAll(objs);
			} else {
				return objs;
			}
		} finally {
			this.close(pm);
		}
	}

	@Override
	public <T> int countObjects(Class<T> klass) {
		return this.countObjectsByQuery(klass, null);
	}

	@Override
	public <T> int countObjectsByQuery(Class<T> klass, String filter) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			Query query = this.prepareQuery(klass, filter, null, pm, true);
			query.setResult("count(this)");
			return (Integer) query.execute();
		} finally {
			this.close(pm);
		}
	}

	/**
	 * Saves the cursor for the given results at the <code>ThreadProperties</code>.
	 */
	protected <T> void saveCursor(List<T> objs) {
		if (this.saveCursor) {
			Cursor cursor = JDOCursorHelper.getCursor(objs);
			ThreadProperties.setProperty(CURSOR_PROPERTY, cursor.toWebSafeString());
		}
	}

	/**
	 * Adjust the query to use the given cursor
	 */
	protected void adjustCursor(Query query) {
		if (this.saveCursor) {
			String cursorStr = (String) ThreadProperties.getProperty(CURSOR_PROPERTY);
			Cursor cursor = Cursor.fromWebSafeString(cursorStr);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
	}

	protected void close(PersistenceManager pm) {
		if (this.detach) {
			pm.close();
		}
	}

	protected <T> Query prepareQuery(Class<T> klass, String filter, String orderingAtts, PersistenceManager pm, boolean onKeys) {
		Query query;
		if (onKeys) {
			query = pm.newQuery("select id from " + klass.getName());
		} else {
			query = pm.newQuery(klass);
		}
		if (filter != null) {
			query.setFilter(filter);
		}
		if (orderingAtts != null) {
			query.setOrdering(orderingAtts);
		}
		if (this.maxSize > 0) {
			query.setRange(0, this.maxSize);
		}
		return query;
	}

	/**
	 * Prepare a query to the given class, using the given filter and ordering atts.
	 */
	private <T> Query prepareQuery(Class<T> klass, String filter, String orderingAtts, PersistenceManager pm) {
		return this.prepareQuery(klass, filter, orderingAtts, pm, false);
	}

	/**
	 * This class is a wrapper to the {@link PersistenceManagerFactory}.
	 */
	protected static class PMFWrapper {
		static {
			System.setProperty("javax.jdo.PersistenceManagerFactoryClass",
					"org.datanucleus.store.appengine.jdo.DatastoreJDOPersistenceManagerFactory");
		}
		private static final PersistenceManagerFactory pmFactory = JDOHelper.getPersistenceManagerFactory("transactions-optional");

		public static PersistenceManagerFactory getPersistenceManagerFactory() {
			return pmFactory;
		}

		public static PersistenceManager getPersistenceManager() {
			return pmFactory.getPersistenceManager();
		}
	}
}
