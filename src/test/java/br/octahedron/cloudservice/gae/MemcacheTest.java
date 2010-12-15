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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.DisabledMemcacheException;
import br.octahedron.cotopaxi.cloudservice.MemcacheFacade;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author nome - email@octahedron.com.br
 * 
 */
public class MemcacheTest {

	private LocalServiceTestHelper helper;
	// = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());;

	private MemcacheFacade facade1;
	private MemcacheFacadeImpl facade2;

	@Before
	public void setUp() {
		this.helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
		this.helper.setUp();
		this.facade1 = new MemcacheFacadeImpl();
		this.facade2 = new MemcacheFacadeImpl("otherNamespace");
	}

	@After
	public void tearDown() {
		this.helper.tearDown();
	}

	@Test
	public void containsPutGetTest() throws DisabledMemcacheException {
		assertFalse(this.facade1.contains("A"));
		this.facade1.put("A", "A");
		assertTrue(this.facade1.contains("A"));
		assertEquals("A", this.facade1.get(String.class, "A"));
		assertNull(this.facade1.get(String.class, "B"));
		this.facade1.put("A", "B");
		assertEquals("B", this.facade1.get(String.class, "A"));
	}

	@Test
	public void memcacheNamespacesTest() throws DisabledMemcacheException {
		assertFalse(this.facade1.contains("A"));
		assertFalse(this.facade2.contains("A"));
		this.facade1.put("A", "A");
		assertFalse(this.facade2.contains("A"));
		assertTrue(this.facade1.contains("A"));
		this.facade2.put("A", "B");
		assertTrue(this.facade2.contains("A"));
		assertEquals("A", this.facade1.get(String.class, "A"));
		assertEquals("B", this.facade2.get(String.class, "A"));
	}

}
