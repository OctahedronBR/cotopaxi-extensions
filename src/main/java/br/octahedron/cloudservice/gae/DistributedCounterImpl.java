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

import br.octahedron.cotopaxi.cloudservice.DistributedCounter;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * A DistributedConter implementation for GAE. It's implemented using the
 * <code>MemcacheService#increment</code> atomic operation.
 * 
 * @see DistributedCounter
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class DistributedCounterImpl implements DistributedCounter {

	private static final String NAMESPACE = "COUNTER";
	private MemcacheService cache;
	private String key;

	/**
	 * 
	 */
	public DistributedCounterImpl(String name) {
		this.cache = MemcacheServiceFactory.getMemcacheService(NAMESPACE);
		this.key = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DistributedCounter#getName()
	 */
	@Override
	public String getName() {
		return this.key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DistributedCounter#count()
	 */
	@Override
	public long count() {
		return this.cache.increment(this.key, 1l, 0l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DistributedCounter#get()
	 */
	@Override
	public long getValue() {
		return this.cache.increment(this.key, 0l, 0l);
	}

}
