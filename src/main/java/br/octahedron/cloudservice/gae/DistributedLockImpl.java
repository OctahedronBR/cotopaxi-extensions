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

import static java.lang.Boolean.TRUE;

import java.util.concurrent.TimeoutException;

import br.octahedron.cotopaxi.cloudservice.DistributedLock;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * DistributedLock implementation for GAE. It's implemented using the
 * <code>MemcacheService#increment</code> atomic operation.
 * 
 * @see DistributedLock
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public class DistributedLockImpl implements DistributedLock {

	private static final String NAMESPACE = "MEMCACHE_LOCK";
	private MemcacheService cache;
	private String key;
	private LockThreadLocal threadLock;

	public DistributedLockImpl(String lockName) {
		this.key = lockName;
		this.threadLock = new LockThreadLocal();
		this.cache = MemcacheServiceFactory.getMemcacheService(NAMESPACE);
	}

	protected void setMemcacheService(MemcacheService memcache) {
		this.cache = memcache;
	}

	/**
	 * Register that current thread has the lock
	 */
	private void registerLock() {
		this.threadLock.set(TRUE);
	}

	/**
	 * Remove the current thread's lock register.
	 */
	private void unregisterLock() {
		this.threadLock.remove();
	}

	/**
	 * Checks if the current thread has the lock
	 * 
	 * @return <code>true</code> if the thread has the lock, <code>false</code> otherwise.
	 */
	private boolean checkLock() {
		return this.threadLock.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.DistributedLock#getName()
	 */
	@Override
	public String getName() {
		return this.key;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see googleservice.MemcacheLock#tryLock()
	 */
	public boolean tryLock() {
		if (!this.checkLock()) {
			// Hum.. i don't have the lock, let's try to acquire it!
			if (this.cache.increment(this.key, 1l, 0l) == 1) {
				this.registerLock();
				return true;
			} else {
				return false;
			}
		} else {
			// I already have the lock!
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see googleservice.MemcacheLock#lock()
	 */
	public void lock() throws TimeoutException {
		try {
			int count = 4;
			while (!this.tryLock() && count != 0) {
				Thread.sleep(2);
				count--;
			}
			if (count == 0) {
				throw new TimeoutException();
			}
		} catch (InterruptedException e) {
			throw new RuntimeException("Somethings went wrong with MemcacheLock!");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see googleservice.MemcacheLock#unlock()
	 */
	public void unlock() {
		if (this.checkLock()) {
			this.cache.increment(this.key, Long.MIN_VALUE, 0l);
			this.unregisterLock();
		}
	}

	private static class LockThreadLocal extends ThreadLocal<Boolean> {
		@Override
		protected Boolean initialValue() {
			return false;
		}
	}

}
