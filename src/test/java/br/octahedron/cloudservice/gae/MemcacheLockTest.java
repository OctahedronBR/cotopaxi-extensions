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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * 
 */
public class MemcacheLockTest {
	private LocalServiceTestHelper helper;
	// = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());;

	private DistributedLockImpl monitor;
	private MemcacheService mockedMemcache;
	private String key;

	@Before
	public void setUp() {
		this.helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
		this.helper.setUp();
		this.key = MemcacheTest.class.getName();
		this.monitor = new DistributedLockImpl(this.key);
		this.mockedMemcache = createMock(MemcacheService.class);
	}

	@After
	public void tearDown() {
		this.helper.tearDown();
	}

	@Test
	public void memcacheLockTest() throws DisabledMemcacheException, TimeoutException {
		assertTrue(this.monitor.tryLock());
		assertTrue(this.monitor.tryLock());
		this.monitor.lock();
		this.monitor.unlock();
		this.monitor.unlock();
	}

	@Test
	public void memcacheLockTest2() throws DisabledMemcacheException, TimeoutException, InterruptedException {
		/*
		 * Tries to unlock a monitor locked by other thread.
		 */
		this.monitor.setMemcacheService(this.mockedMemcache);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(1l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(2l);
		replay(this.mockedMemcache);

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					MemcacheLockTest.this.monitor.lock();
				} catch (Exception e) {
					fail();
				}
			}
		});
		t.start();
		t.join();
		this.monitor.unlock();
		assertFalse(this.monitor.tryLock());
		verify(this.mockedMemcache);
	}

	@Test(expected = TimeoutException.class)
	public void memcacheLockTest3() throws DisabledMemcacheException, TimeoutException, InterruptedException {
		/*
		 * Tries to lock a monitor and gets a TimeoutExeption
		 */
		this.monitor.setMemcacheService(this.mockedMemcache);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(1l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(2l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(3l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(4l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(5l);
		expect(this.mockedMemcache.increment(this.key, 1l, 0l)).andReturn(6l);
		replay(this.mockedMemcache);
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					assertTrue(MemcacheLockTest.this.monitor.tryLock());
				} catch (Exception e) {
					fail();
				}
			}
		});
		t.start();
		t.join();
		// Timeout Here
		this.monitor.lock();
		verify(this.mockedMemcache);
	}
}
