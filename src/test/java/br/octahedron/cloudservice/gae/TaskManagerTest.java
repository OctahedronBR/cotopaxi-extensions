package br.octahedron.cloudservice.gae;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.octahedron.cotopaxi.cloudservice.common.Task;

import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

public class TaskManagerTest {

	private static final String URL_PREFIX = "/test";
	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalTaskQueueTestConfig());
	private TaskManagerFacadeImpl taskMng;

	@Before
	public void setUp() {
		this.helper.setUp();
		this.taskMng = new TaskManagerFacadeImpl(URL_PREFIX);
	}

	@After
	public void tearDown() {
		this.helper.tearDown();
	}

	@Test
	public void addTaskTest1() {
		Task t1 = new TestTask("task1", getParams());

		this.taskMng.add(t1, 10);

		LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
		assertEquals(1, qsi.getTaskInfo().size());
		assertEquals("task1", qsi.getTaskInfo().get(0).getTaskName());
	}

	@Test
	public void addTaskTest2() {
		Task t2 = new TestTask(getParams());
		this.taskMng.add(t2, 20, "myQueue");

		LocalTaskQueue ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
		QueueStateInfo qsi = ltq.getQueueStateInfo().get(QueueFactory.getDefaultQueue().getQueueName());
		assertEquals(1, qsi.getTaskInfo().size());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void createQueue1() {
		this.taskMng.createQueue("test");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void createQueue2() {
		this.taskMng.createQueue("test", getParams());
	}

	private static Map<String, String> getParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("company", "octahedron");
		return params;
	}

	private static class TestTask extends Task {
		public TestTask(String name, Map<String, String> params) {
			super(name, params);
		}

		public TestTask(Map<String, String> params) {
			super(params);
		}

		@Override
		public void run() {
		}
	}
}
