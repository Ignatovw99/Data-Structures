package core;

import model.ScheduledTask;
import model.Task;
import org.junit.Before;
import org.junit.Test;
import shared.Scheduler;

import static org.junit.Assert.*;

public class ProcessSchedulerTest {
    private Scheduler scheduler;

    @Before
    public void setUp() {
        this.scheduler = new ProcessScheduler();
        for (int i = 1; i <= 20; i++) {
            this.scheduler.add(new ScheduledTask(i, "test_description"));
        }
    }

    @Test
    public void testPeekOnSingleElement() {
        Scheduler scheduler = new ProcessScheduler();
        scheduler.add(new ScheduledTask(100, "test_description"));
        Task task = scheduler.peek();
        assertNotNull(task);
        assertEquals(100, task.getId());
    }

    @Test
    public void testPeekOnMultipleElement() {
        Scheduler scheduler = new ProcessScheduler();
        scheduler.add(new ScheduledTask(73, "test_description"));
        for (int i = 1; i <= 20; i++) {
            scheduler.add(new ScheduledTask(i, "test_description"));
        }
        scheduler.add(new ScheduledTask(100, "test_description"));
        Task task = scheduler.peek();
        assertNotNull(task);
        assertEquals(73, task.getId());
    }

    @Test
    public void testAddOnMultipleElement() {
        Task task = this.scheduler.peek();
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals(20, this.scheduler.size());

        int expectedId = 1;
        while (this.scheduler.size() > 0) {
            Task process = this.scheduler.process();
            assertNotNull(process);
            assertEquals(expectedId++, process.getId());
        }
        assertEquals(21, expectedId);
    }

    @Test
    public void testAddOnSingleElement() {
        Scheduler scheduler = new ProcessScheduler();
        assertNull(scheduler.peek());
        assertEquals(0, scheduler.size());

        scheduler.add(new ScheduledTask(1, "test_description"));

        assertNotNull(scheduler.peek());
        assertEquals(1, scheduler.peek().getId());
        assertEquals(1, scheduler.size());
    }

    @Test
    public void testPeekShouldReturnCorrectAndShouldNotRemove() {
        Scheduler scheduler = new ProcessScheduler();
        scheduler.add(new ScheduledTask(42, "test_description"));
        for (int i = 1; i <= 20; i++) {
            scheduler.add(new ScheduledTask(i, "test_description"));
        }
        Task task = scheduler.peek();
        assertNotNull(task);
        assertEquals(42, task.getId());
        assertEquals(21, scheduler.size());
        task = scheduler.peek();
        assertNotNull(task);
        assertEquals(42, task.getId());
        assertEquals(21, scheduler.size());
    }

    @Test
    public void testProcessShouldReturnCorrectAndShouldRemove() {
        Scheduler scheduler = new ProcessScheduler();
        scheduler.add(new ScheduledTask(42, "test_description"));
        for (int i = 1; i <= 20; i++) {
            scheduler.add(new ScheduledTask(i, "test_description"));
        }
        Task task = scheduler.process();
        assertNotNull(task);
        assertEquals(42, task.getId());
        assertEquals(20, scheduler.size());
        task = scheduler.process();
        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals(19, scheduler.size());
    }

    @Test
    public void toArrat() {
        Task[] tasks = scheduler.toArray();
        assertEquals(tasks.length , 20);
    }


    @Test
    public void  insertBefore() {
        scheduler.insertBefore(20, new ScheduledTask(21, "asd"));
        scheduler.insertBefore(1, new ScheduledTask(22, "asd"));
    }

    @Test
    public void  testAfter() {
        scheduler.insertAfter(20, new ScheduledTask(21, "asd"));
        scheduler.insertAfter(1, new ScheduledTask(22, "asd"));
    }
}