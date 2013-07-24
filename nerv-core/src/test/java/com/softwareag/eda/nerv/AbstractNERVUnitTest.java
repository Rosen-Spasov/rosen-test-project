package com.softwareag.eda.nerv;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.softwareag.eda.nerv.connection.NERVConnection;
import com.softwareag.eda.nerv.consumer.BasicConsumer;
import com.softwareag.eda.nerv.consumer.FilteredConsumer;
import com.softwareag.eda.nerv.consumer.TestConsumer;
import com.softwareag.eda.nerv.subscribe.subscription.DefaultSubscription;
import com.softwareag.eda.nerv.task.PublishTask;

public class AbstractNERVUnitTest {

	protected String type = "myType";

	protected String body = "myMessage";

	protected NERVConnection connection;

	@Before
	public void before() throws Exception {
		connection = NERV.instance().getDefaultConnection();
	}

	@Test
	public void testPubSub() throws Exception {
		pubSub(1);
	}

	protected void pubSub(int expectedMessages) throws Exception {
		pubSub(expectedMessages, 1, 1, 15 * 1000);
	}

	protected void pubSubUsingFilter(int expectedMessages) throws Exception {
		pubSub(expectedMessages, 1, 1, 15 * 1000, true);
	}

	protected void pubSub(int expectedMessages, int consumersCount, int threadsCount) throws Exception {
		pubSub(expectedMessages, consumersCount, threadsCount, 15 * 1000);
	}

	protected void pubSub(int expectedMessages, int consumersCount, int threadsCount, int timeout) throws Exception {
		pubSub(expectedMessages, consumersCount, threadsCount, timeout, false);
	}

	protected void pubSub(int expectedMessages, int consumersCount, int threadsCount, int timeout, boolean filter) throws Exception {
		List<TestConsumer> consumers = new ArrayList<TestConsumer>();
		for (int count = 0; count < consumersCount; count++) {
			TestConsumer consumer = filter ? new FilteredConsumer(expectedMessages, body) : new BasicConsumer(expectedMessages);
			connection.subscribe(new DefaultSubscription(type, consumer));
			consumers.add(consumer);
		}
		Thread.sleep(500);
		int msgsPerThread = expectedMessages / threadsCount;
		for (int count = 0; count < threadsCount; count++) {
			new Thread(new PublishTask(type, body, msgsPerThread)).start();
		}
		try {
			for (TestConsumer consumer : consumers) {
				if (consumer.getEvents().size() < expectedMessages) {
					synchronized (consumer.getLock()) {
						consumer.getLock().wait(timeout);
					}
				}
				assertEquals(expectedMessages, consumer.getEvents().size());
				assertEquals(body, consumer.getEvents().get(0).getBody());
			}
		} finally {
			for (TestConsumer consumer : consumers) {
				connection.unsubscribe(new DefaultSubscription(type, consumer));
			}
		}
	}

}
