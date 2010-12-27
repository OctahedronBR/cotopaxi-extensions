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

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.Map;

import br.octahedron.cotopaxi.cloudservice.TaskManagerFacade;
import br.octahedron.cotopaxi.cloudservice.common.Task;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * @author Danilo Penna Queiroz - daaniloqueiroz@octahedron.com.br
 * 
 */
public class TaskManagerFacadeImpl implements TaskManagerFacade {

	private static final String URL_SEPARATOR = "/";
	private String urlPrefix;

	public TaskManagerFacadeImpl(String urlPrefix) {
		if (!urlPrefix.endsWith(URL_SEPARATOR)) {
			urlPrefix += URL_SEPARATOR;
		}
		this.urlPrefix = urlPrefix;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.TaskManager#add(br.octahedron.cs.common.Task, long)
	 */
	@Override
	public void add(Task task, long delayMillis) {
		this.add(this.getTaskOptions(task, delayMillis), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.TaskManager#add(br.octahedron.cs.common.Task, long, java.lang.String)
	 */
	@Override
	public void add(Task task, long delayMillis, String queueName) {
		this.add(this.getTaskOptions(task, delayMillis), queueName);
	}

	/**
	 */
	private void add(TaskOptions taskOptions, String queueName) {
		Queue queue;
		if (queueName == null) {
			queue = QueueFactory.getDefaultQueue();
		} else {
			queue = QueueFactory.getQueue(queueName);
		}

		try {
			queue.add(taskOptions);
		} catch (IllegalStateException e) {
			queue = QueueFactory.getDefaultQueue();
			queue.add(taskOptions);
		}
	}

	private TaskOptions getTaskOptions(Task task, long delayMillis) {
		TaskOptions options = withUrl(this.urlPrefix + task.getClass().getName());
		options = options.countdownMillis(delayMillis);
		if (task.getName() != null) {
			options = options.taskName(task.getName());
		}
		for (String paramName : task.getParamsNames()) {
			options = options.param(paramName, task.getParam(paramName));
		}

		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.TaskManager#createQueue(java.lang.String)
	 */
	@Override
	public void createQueue(String queueName) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by Google App Engine");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.octahedron.cs.TaskManager#createQueue(java.lang.String, java.util.Map)
	 */
	@Override
	public void createQueue(String queueName, Map<String, String> queueProperties) throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Operation not supported by Google App Engine");
	}

}
