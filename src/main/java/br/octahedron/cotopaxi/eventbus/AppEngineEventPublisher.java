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

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import br.octahedron.cotopaxi.inject.Injector;
import br.octahedron.util.Log;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * An {@link EventPublisher} implementation using Google App Engine Tasks Queues Service.
 * 
 * @author Danilo Queiroz
 */
public class AppEngineEventPublisher implements EventPublisher {

	private static final String QUEUE_NAME = "eventbus";
	private static final Log log = new Log(AppEngineEventPublisher.class);
	private Queue taskQueue;

	public AppEngineEventPublisher() {
		this(QueueFactory.getQueue(QUEUE_NAME));
	}

	public AppEngineEventPublisher(Queue queue) {
		this.taskQueue = queue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.straight.eventbus.Enqueuer#enqueue(java.util.Collection,
	 * br.octahedron.straight.eventbus.Event)
	 */
	@Override
	public void publish(Collection<Class<? extends Subscriber>> subscribers, Event event) {
		Collection<TaskOptions> tasks = new LinkedList<TaskOptions>();
		for (Class<? extends Subscriber> subscriber : subscribers) {
			tasks.add(TaskOptions.Builder.withPayload(new PublishTask(subscriber, event)));
		}
		log.debug("Adding %d PublishTasks to queue %s. EventClass: %s", tasks.size(), QUEUE_NAME, event.getClass());
		this.taskQueue.add(tasks);

	}

	/**
	 * A simple {@link DeferredTask} that publishes an event to a {@link Subscriber}
	 */
	protected static class PublishTask implements DeferredTask, Runnable, Serializable {

		private static final long serialVersionUID = 7900664974046236811L;
		private Class<? extends Subscriber> subscriber;
		private Event event;

		protected PublishTask(Class<? extends Subscriber> subscriber, Event event) {
			this.subscriber = subscriber;
			this.event = event;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			try {
				Subscriber sub = Injector.createInstance(this.subscriber);
				log.debug("Publishing event %s to subscriber %s", this.event.getClass(), this.subscriber.getClass());
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
			if (obj instanceof PublishTask) {
				PublishTask other = (PublishTask) obj;
				return this.subscriber.equals(other.subscriber) && this.event.equals(other.event);
			} else {
				return false;
			}
		}
	}
}
