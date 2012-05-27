package br.octahedron.cotopaxi.eventbus.test;

import static br.octahedron.cotopaxi.inject.DependencyManager.registerDependency;
import static br.octahedron.cotopaxi.inject.DependencyManager.registerImplementation;
import static br.octahedron.cotopaxi.inject.DependencyManager.removeImplementation;

import java.util.Collection;

import br.octahedron.cotopaxi.eventbus.Event;
import br.octahedron.cotopaxi.eventbus.EventBus;
import br.octahedron.cotopaxi.eventbus.EventPublisher;
import br.octahedron.cotopaxi.eventbus.Subscriber;
import br.octahedron.cotopaxi.eventbus.ThreadPoolEventPublisher;
import br.octahedron.cotopaxi.inject.Injector;
import br.octahedron.cotopaxi.test.CotopaxiTestHelper;
import br.octahedron.util.Log;

/**
 * Utility Helper class for tests using {@link EventBus}. It provides way to
 * choose which {@link EventPublisher} should be used by tests.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class EventBusPublisherTestHelper {

	/**
	 * Creates and register a {@link EventPublisher} mock
	 * 
	 * @param testHelper
	 *            The {@link CotopaxiTestHelper} that will manage the created
	 *            mock
	 * @return The {@link EventPublisher} mock object.
	 */
	public static EventPublisher mockEventPublisher(CotopaxiTestHelper testHelper) {
		EventPublisher ep = testHelper.createMock(EventPublisher.class);
		removeImplementation(EventPublisher.class);
		removeImplementation(EventBus.class);
		registerImplementation(EventPublisher.class, ep);
		return ep;
	}

	/**
	 * Register a {@link ThreadPoolEventPublisher} to be used as default
	 * EventPublisher
	 */
	public static void useThreadPoolEventPublisher() {
		removeImplementation(EventPublisher.class);
		removeImplementation(EventBus.class);
		registerDependency(EventPublisher.class, ThreadPoolEventPublisher.class);
	}

	/**
	 * Register a synchronous {@link EventPublisher} to be used. This
	 * {@link EventPublisher} doesn't utilizes multithreading, publishing events
	 * on the same thread in a sequential way. It not intent to be used on
	 * production environments, but is good for tests.
	 */
	public static void useSynchronousEventPublisher() {
		removeImplementation(EventPublisher.class);
		removeImplementation(EventBus.class);
		registerImplementation(EventPublisher.class, new EventPublisher() {
			private final Log log = new Log();

			@Override
			public void publish(Collection<Class<? extends Subscriber>> subscribers, Event event) {
				for (Class<? extends Subscriber> subscriber : subscribers) {
					try {
						Subscriber sub = Injector.getInstance(subscriber);
						this.log.debug("Publishing event %s to subscriber %s", event.getClass(), subscriber);
						sub.eventPublished(event);
					} catch (Exception e) {
						log.warning("Unable to deliver event to subscriber: %s", subscriber.getName());
						log.warning("Unable to deliver event to subscriber", e);
					}
				}
			}
		});
	}

}