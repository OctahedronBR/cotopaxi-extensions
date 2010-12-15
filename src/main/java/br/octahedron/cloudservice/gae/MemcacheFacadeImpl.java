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
package br.octahedron.cloudservice.gae;

import java.util.concurrent.TimeUnit;

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceException;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.StrictErrorHandler;

/**
 * MemcacheFacade implementation for GAE.
 * 
 * @see MemcacheFacade
 * 
 * @author Danilo Penna Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class MemcacheFacadeImpl implements MemcacheFacade {

	private MemcacheService cache;
	private Expiration expire = null;

	public MemcacheFacadeImpl() {
		this(null);
	}

	public MemcacheFacadeImpl(String namespace) {
		this.cache = MemcacheServiceFactory.getMemcacheService(namespace);
		this.cache.setErrorHandler(new StrictErrorHandler());
	}

	public MemcacheFacadeImpl(String namespace, TimeUnit unit, long expirationTime) {
		this(namespace);
		this.expire = Expiration.byDeltaMillis((int) unit.toMillis(expirationTime));
	}

	public MemcacheFacadeImpl(TimeUnit unit, long expirationTime) {
		this();
		this.expire = Expiration.byDeltaMillis((int) unit.toMillis(expirationTime));
	}

	@Override
	public boolean contains(String key) {
		return this.cache.contains(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> klass, String key) throws DisabledMemcacheException {
		try {
			return (T) this.cache.get(key);
		} catch (MemcacheServiceException e) {
			throw new DisabledMemcacheException();
		}
	}

	@Override
	public <T> void put(String key, T value) throws DisabledMemcacheException {
		try {
			this.cache.put(key, value, this.expire);
		} catch (MemcacheServiceException e) {
			throw new DisabledMemcacheException();
		}
	}

	@Override
	public void remove(String key) {
		this.cache.delete(key);
	}

}
