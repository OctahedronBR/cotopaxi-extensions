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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOFatalUserException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.PrimaryKey;

import br.octahedron.cloudservice.gae.ReadOnlyDatastoreException;
import br.octahedron.cotopaxi.cloudservice.CloudServicesFactory;
import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;
import br.octahedron.util.reflect.ReflectionUtil;

/**
 * @author nome - email@octahedron.com.br
 * 
 */
public class CachedDatastoreFacadeImpl extends DatastoreFacadeImpl {

	private MemcacheFacade memcache;

	private CachedDatastoreFacadeImpl(CloudServicesFactory factory) {
		this.memcache = factory.createMemcacheFacade();
	}

	@SuppressWarnings("unchecked")
	public <T> Collection<Object> getObjectsKeys(Class<T> klass, String filter, String orderingAtts) {
		PersistenceManager pm = this.pool.getPersistenceManagerForThread();
		try {
			Query query = this.prepareQuery(klass, filter, orderingAtts, pm, true);
			this.adjustCursor(query);
			List<Object> keys = null;
			try {
				keys = (List<Object>) query.execute();
			} catch (JDOFatalUserException e) {
				// if occurs any error using cursor (invalid cursor)
				query = this.prepareQuery(klass, filter, orderingAtts, pm, true);
				keys = (List<Object>) query.execute();
			}
			this.saveCursor(keys);
			return keys;
		} finally {
			this.close(pm);
		}
	}

	private Object getPrimaryKey(Object object) {
		return ReflectionUtil.getAnnotatedFieldValue(object, PrimaryKey.class);
	}

	private void populateCache(Object... objects) {
		/*
		 * creates a map with the objects' keys and objects stores it at memcache
		 */
	}

	private void removeFromCache(Object... keys) {

	}

	@Override
	public <T> void saveObject(T persistentObject) throws ReadOnlyDatastoreException {
		// saves to datastore
		super.saveObject(persistentObject);
		// puts on cache
		this.populateCache(persistentObject);
	}

	@Override
	public <T> void saveAllObjects(java.util.Collection<T> persistentObjects) throws ReadOnlyDatastoreException {
		// saves to datastore
		super.saveAllObjects(persistentObjects);
		// puts on cache
		this.populateCache(persistentObjects);
	}

	@Override
	public <T> void deleteObject(T persistentObject) throws ReadOnlyDatastoreException {
		// removes from cache
		this.removeFromCache(this.getPrimaryKey(persistentObject));
		// deletes from datastore
		super.deleteObject(persistentObject);
	}

	@Override
	public <T> void deleteObjects(Class<T> klass) throws ReadOnlyDatastoreException {
		// removes from cache
		this.removeFromCache(this.getObjectsKeys(klass, null, null));
		// delete from datastore
		super.deleteObjects(klass);
	}

	@Override
	public <T> void deleteAllObjects(Collection<T> persistentObjects) throws ReadOnlyDatastoreException {
		Collection<Object> keys = new ArrayList<Object>();
		for (T obj : persistentObjects) {
			keys.add(this.getPrimaryKey(obj));
		}
		this.removeFromCache(keys.toArray());
		// delete from datastore
		super.deleteAllObjects(persistentObjects);
	}

	@Override
	public <T> void deleteObjectsByQuery(Class<T> klass, String filter) throws ReadOnlyDatastoreException {
		// removes from cache
		this.removeFromCache(this.getObjectsKeys(klass, filter, null));
		// delete from datastore
		super.deleteObjectsByQuery(klass, filter);
	}

	@Override
	public <T> T getObjectByKey(Class<T> klass, Object key) {
		T obj = null;
		try {
			String keyStr = key.toString();
			if (this.memcache.contains(keyStr)) {
				return this.memcache.get(klass, keyStr);
			} else {
				obj = super.getObjectByKey(klass, key);
				this.memcache.put(key.toString(), obj);
				return obj;
			}
		} catch (DisabledMemcacheException e) {
			// cache is disabled. Try from datastore
			if (obj == null) {
				obj = super.getObjectByKey(klass, key);
			}
			return obj;
		}
	}

	/*
	 * When querying, i just load the keys and returns an special collection that binds to
	 * cachedatastore facade and load object by object using the getObjectByKey
	 */

	/*
	 * getObjects(Class<T>) getObjectsByQuery(Class<T>, String) getObjectsByQuery(Class<T>, String,
	 * String)
	 */

}
