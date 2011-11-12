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

import java.util.Collection;

import javax.jdo.Query;

/**
 * An Generic DAO, that provide basic operations for a Type.
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 */
public abstract class GenericDAO<T> {

	protected DatastoreFacade datastoreFacade = new DatastoreFacade();
	private Class<T> klass;
	
	public GenericDAO(Class<T> klass) {
		this.klass = klass;
	}

	/**
	 * @param datastoreFacade the datastoreFacade to set
	 */
	public void setDatastoreFacade(DatastoreFacade datastoreFacade) {
		this.datastoreFacade = datastoreFacade;
	}
	
	/**
	 * Creates a new query for this DAO entities.
	 * 
	 * @return A new query for entities from this DAO.
	 */
	protected Query createQuery() {
		return this.datastoreFacade.createQueryForClass(this.klass);
	}
	
	/**
	 * Deletes an object. The given parameter can be the object to be deleted, it means an instance
	 * of T, or can be the key for the object to be deleted.
	 * 
	 * @param object
	 *            The object to be deleted or the key for the object to be deleted.
	 */
	public void delete(Object object) {
		if (object.getClass().equals(this.klass)) {
			this.datastoreFacade.deleteObject(object);
		} else {
			if (this.exists(object)) {
				this.delete(this.get(object));
			}
		}
	}

	/**
	 * Saves an entity. It doesn't verify if the object already exists, it just saves, overwriting
	 * the previous object, if exists.
	 * 
	 * @param entity
	 *            The entity to be save
	 */
	public void save(T entity) {
		this.datastoreFacade.saveObject(entity);
	}

	/**
	 * @return Gets all T entities.
	 */
	public Collection<T> getAll() {
		return this.datastoreFacade.getObjects(this.klass);
	}

	/**
	 * Get all elements which given attribute starts with the given prefix;
	 * 
	 * @param prefix the attribute's prefix to be queried
	 * @param attribute the entity attribute being queried
	 * 
	 * 
	 * @return a list of all entities which attribute starts with the given prefix. If no entity be found, it returns an empty list.
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> getAllStartsWith(String attribute, String prefix) {
		Query query = this.createQuery();
		query.setFilter(attribute + ".startsWith(:1)");
		prefix = (prefix != null ? prefix : "").trim();
		return (Collection<T>) query.execute(prefix);
	}

	/**
	 * Gets an T entity.
	 * 
	 * @param key
	 *            the entity's key.
	 * @return The entity with the given key, if exists, null otherwise.
	 */
	public T get(Object key) {
		return this.datastoreFacade.getObjectByKey(this.klass, key);
	}
	
	/**
	 * Creates a new Query on the database for this DAO's class.
	 * The returned query can be customized by the application to
	 * find the desired entries in the database.
	 * 
	 * @return The created Query
	 * @see javax.jdo.Query
	 * @since 1.1
	 */
	public Query createQuery() {
		return this.datastoreFacade.createQueryForClass(klass);
	}

	/**
	 * Checks if exists an entity with the given key.
	 * 
	 * @param key
	 *            the entity's key
	 * @return <code>true</code> if exists, <code>false</code> otherwise.
	 */
	public boolean exists(Object key) {
		return this.datastoreFacade.existsObject(this.klass, key);
	}

	/**
	 * @return The number of T entities stored
	 */
	public int count() {
		return this.datastoreFacade.countObjects(this.klass);
	}
	

}
