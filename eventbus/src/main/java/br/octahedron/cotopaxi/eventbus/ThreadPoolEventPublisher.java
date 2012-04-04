package br.octahedron.cotopaxi.eventbus;

import static java.util.concurrent.Executors.defaultThreadFactory;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import br.octahedron.cotopaxi.inject.Injector;
import br.octahedron.util.Log;

/**
 * The base {@link EventPublisher} implementation using {@link Thread}.
 * 
 * It uses a {@link ExecutorService} with fixed size to schedule the
 * {@link Thread}. It also can use a custom {@link ThreadFactory} to create the
 * tasks.
 * 
 * @see ExecutorService
 * @See ThreadFactory
 * @see Executors#newFixedThreadPool(int, ThreadFactory)
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class ThreadPoolEventPublisher implements EventPublisher {

	private static final int DEFAULT_THREAD_POOL_SIZE = 20;
	private static final Log log = new Log(ThreadPoolEventPublisher.class);
	private ExecutorService threadPool;

	public ThreadPoolEventPublisher() {
		this(DEFAULT_THREAD_POOL_SIZE);
	}

	public ThreadPoolEventPublisher(ThreadFactory thFactory) {
		this(DEFAULT_THREAD_POOL_SIZE, thFactory);
	}

	public ThreadPoolEventPublisher(int i) {
		this(i, defaultThreadFactory());
	}

	/**
	 * @param size
	 * @param defaultThreadFactory
	 */
	public ThreadPoolEventPublisher(int size, ThreadFactory thFactory) {
		this.threadPool = newFixedThreadPool(size, thFactory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.octahedron.straight.eventbus.Enqueuer#enqueue(java.util.Collection,
	 * br.octahedron.straight.eventbus.Event)
	 */
	public void publish(Collection<Class<? extends Subscriber>> subscribers, Event event) {
		for (Class<? extends Subscriber> subscriber : subscribers) {
			this.threadPool.execute(new PublishThread(subscriber, event));
			log.debug("Thread to process event %s by %s added to thread pool.", event.getClass(), subscriber);
		}
	}

	/**
	 * A {@link Runnable} that publishes an event to a {@link Subscriber}
	 */
	protected static class PublishThread implements Runnable, Serializable {

		private static final long serialVersionUID = 7900664974046236811L;
		private Class<? extends Subscriber> subscriber;
		private Event event;

		protected PublishThread(Class<? extends Subscriber> subscriber, Event event) {
			this.subscriber = subscriber;
			this.event = event;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			try {
				Subscriber sub = Injector.getInstance(this.subscriber);
				log.debug("Publishing event %s to subscriber %s", this.event.getClass(), this.subscriber);
				sub.eventPublished(this.event);
			} catch (Exception e) {
				log.warning("Unable to deliver event to subscriber: %s", subscriber.getName());
				log.warning("Unable to deliver event to subscriber", e);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return this.subscriber.hashCode() ^ this.event.hashCode();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PublishThread) {
				PublishThread other = (PublishThread) obj;
				return this.subscriber.equals(other.subscriber) && this.event.equals(other.event);
			} else {
				return false;
			}
		}
	}
}