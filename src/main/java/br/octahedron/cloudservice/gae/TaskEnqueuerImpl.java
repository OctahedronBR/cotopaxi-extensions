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

import java.util.Map.Entry;

import br.octahedron.cotopaxi.cloudservice.Task;
import br.octahedron.cotopaxi.cloudservice.TaskEnqueuer;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * @author Danilo Penna Queiroz - daaniloqueiroz@octahedron.com.br
 * 
 */
public class TaskEnqueuerImpl implements TaskEnqueuer {

	@Override
	public void enqueue(Task task) {
		String queueName = task.getQueue();
		this.enqueue(this.getTaskOptions(task), queueName);
	}

	/**
	 */
	private void enqueue(TaskOptions taskOptions, String queueName) {
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

	private TaskOptions getTaskOptions(Task task) {
		TaskOptions options = withUrl(task.getUrl());
		options = options.method(TaskOptions.Method.POST);
		options = options.countdownMillis(task.getDelay());
		for (Entry<String, String> param : task.getParams().entrySet()) {
			options = options.param(param.getKey(), param.getValue());
		}
		return options;
	}

}
