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
package br.octahedron.cotopaxi.eventbus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Danilo Queiroz
 */
public class EventBusTest {

	private Subscriber consumerOne;
	private Subscriber consumerTwo;
	private EventPublisher publisher;
	private EventBus bus = new EventBus();

	@Before
	public void setUp() {
		this.publisher = createMock(EventPublisher.class);
		this.consumerOne = createMock(Subscriber.class);
		this.consumerTwo = createMock(Subscriber.class);
		bus.reset();
		bus.setEventPublisher(this.publisher);
	}

//	@Test
//	public void consumeTest() {
//		Event event = new EventOne();
//		new AppEngineEventPublisher.PublishTask(SubscriberOne.class, event).run();
//		assertEquals(EventOne.class, SubscriberOne.receivedEvent.getClass());
//	}

	@Test
	public void subscribeTest() {
		replay(this.consumerOne, this.consumerTwo, this.publisher);

		assertFalse(bus.subscribers.containsKey(EventOne.class));
		assertFalse(bus.subscribers.containsKey(EventTwo.class));

		bus.subscribe(SubscriberOne.class);
		bus.subscribe(SubscriberTwo.class);

		assertTrue(bus.subscribers.containsKey(EventOne.class));
		assertEquals(1, bus.subscribers.get(EventOne.class).size());
		assertTrue(bus.subscribers.containsKey(EventTwo.class));
		assertEquals(2, bus.subscribers.get(EventTwo.class).size());

		verify(this.consumerOne, this.consumerTwo, this.publisher);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void publishTest() {
		/*
		 * How this test can be better? I've tried to do a better mock setup, specifying the
		 * parameters to be passed, but with out success. EasyMock doesn't recognize the both lists
		 * (the passed one and the one that EventBus passes) as equals. Sadly.
		 */
		Event event = new EventTwo();
		this.publisher.publish(Arrays.asList(SubscriberOne.class, SubscriberTwo.class), event);
		replay(this.consumerOne, this.consumerTwo, this.publisher);

		bus.subscribe(SubscriberOne.class);
		bus.subscribe(SubscriberTwo.class);
		bus.publish(event);

		verify(this.consumerOne, this.consumerTwo, this.publisher);
	}
}
