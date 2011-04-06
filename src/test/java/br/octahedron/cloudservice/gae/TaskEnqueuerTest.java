package br.octahedron.cloudservice.gae;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.TaskEnqueuer;
import br.octahedron.cotopaxi.cloudservice.Task;
import br.octahedron.cotopaxi.inject.InjectionManager;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class TaskEnqueuerTest {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalTaskQueueTestConfig());

	@Before
	public void setUp() {
		this.helper.setUp();
		InjectionManager.registerDependency(TaskEnqueuer.class, TaskEnqueuerImpl.class);
	}

	@After
	public void tearDown() {
		this.helper.tearDown();
	}

	@Test
	public void addTaskTest1() {
		Task t1 = new Task("/task1", getParams());
		t1.enqueue();

		LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
		assertEquals(1, qsi.getTaskInfo().size());
		assertEquals("/task1", qsi.getTaskInfo().get(0).getUrl());
	}

	@Test
	public void addTaskTest2() {
		Task t2 = new Task("/task2", getParams(),"myQueue", 20);
		t2.enqueue();

		LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
		assertEquals(1, qsi.getTaskInfo().size());
		assertEquals("/task2", qsi.getTaskInfo().get(0).getUrl());
	}

	private static Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("company", "octahedron");
		return params;
	}
}
