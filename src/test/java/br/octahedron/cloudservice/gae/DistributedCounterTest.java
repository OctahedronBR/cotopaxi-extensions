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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.DistributedCounter;

import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * @author nome - email@octahedron.com.br
 * 
 */
public class DistributedCounterTest {

	private LocalServiceTestHelper helper;
	private DistributedCounter distributedCounter1;
	private DistributedCounter distributedCounter2;

	@Before
	public void setUp() {
		this.helper = new LocalServiceTestHelper(new LocalMemcacheServiceTestConfig());
		this.helper.setUp();
		this.distributedCounter1 = new DistributedCounterImpl("visitors");
		this.distributedCounter2 = new DistributedCounterImpl("clicks");
	}

	@After
	public void tearDown() {
		this.helper.tearDown();
	}

	@Test
	public void testSimpleUserCount() {
		assertEquals(0l, this.distributedCounter1.getValue());
		assertEquals(1l, this.distributedCounter1.count());
		assertEquals(2l, this.distributedCounter1.count());
		assertEquals(2l, this.distributedCounter1.getValue());

		assertEquals(0l, this.distributedCounter2.getValue());
		assertEquals(1l, this.distributedCounter2.count());
		assertEquals(1l, this.distributedCounter2.getValue());
		assertEquals(1l, this.distributedCounter2.getValue());
	}

}
