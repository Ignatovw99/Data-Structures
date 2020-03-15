package implementations;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    private ArrayDeque<Integer> arrayDeque;

    @Before
    public void setup() {
        arrayDeque = new ArrayDeque<>();
    }

    @Test
    public void testInitializingArrayDeque() {
        assertEquals(7, arrayDeque.capacity());
    }

    @Test
    public void testAdd() {
        arrayDeque.add(12);
        arrayDeque.add(13);
        arrayDeque.add(14);
        assertEquals(3, arrayDeque.size());
    }

    @Test
    public void testResizing() {
        arrayDeque.add(12);
        arrayDeque.add(13);
        arrayDeque.add(14);
        arrayDeque.add(15);
        assertEquals(7, arrayDeque.capacity());
    }

    @Test
    public void testAddFirst() {
        arrayDeque.addFirst(12);
        arrayDeque.addFirst(13);
        arrayDeque.addFirst(14);
        arrayDeque.addFirst(15);
        assertEquals(4, arrayDeque.size());
    }

    @Test
    public void testPeekAsQueue() {
        arrayDeque.offer(12);
        arrayDeque.offer(13);
        arrayDeque.offer(14);
        int actual = arrayDeque.peek();
        assertEquals(12, actual);
    }

    @Test
    public void testPeekAsStack() {
        arrayDeque.push(12);
        arrayDeque.push(13);
        arrayDeque.push(14);
        int actual = arrayDeque.peek();
        assertEquals(14, actual);
    }

    @Test
    public void testPollAsQueue() {
        arrayDeque.offer(12);
        arrayDeque.offer(13);
        arrayDeque.offer(14);
        int actual = arrayDeque.poll();
        assertEquals(12, actual);
    }

    @Test
    public void testPollAsStack() {
        arrayDeque.push(12);
        arrayDeque.push(13);
        arrayDeque.push(14);
        int actual = arrayDeque.poll();
        assertEquals(14, actual);
    }

    @Test
    public void testInsert() {
        arrayDeque.add(12);
        arrayDeque.add(13);
        arrayDeque.add(14);
        arrayDeque.add(15);
        arrayDeque.add(16);
        arrayDeque.insert(1, 43);
        assertEquals(43, arrayDeque.get(1).intValue());
        assertEquals(14, arrayDeque.get(3).intValue());
    }

}