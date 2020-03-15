package implementations;

import interfaces.Deque;

import java.util.Iterator;

public class ArrayDeque<E> implements Deque<E> {

    private final int INITIAL_CAPACITY = 3;

    private int size;

    private int head;

    private int tail;

    private Object[] elements;

    public ArrayDeque() {
        elements = new Object[INITIAL_CAPACITY];
        int middle = elements.length / 2;
        head = tail = middle;
    }

    @Override
    public void add(E element) {
        addLast(element);
    }

    private Object[] resize() {
        int newCapacity = elements.length * 2 + 1;
        Object[] newElements = new Object[newCapacity];
        int middle = newElements.length / 2;
        int begin = middle - size / 2;
        int currentIndex = head;
        for (int i = begin; currentIndex <= tail; i++) {
            newElements[i] = elements[currentIndex++];
        }
        head = begin;
        tail = head + size - 1;
        return newElements;
    }

    @Override
    public void offer(E element) {
        addLast(element);
    }

    @Override
    public void addFirst(E element) {
        if (head == 0 || head == elements.length) {
            elements = resize();
        }
        if (isEmpty()) {
            elements[head] = element;
        } else {
            elements[--head] = element;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (tail == 0 || tail == elements.length - 1) {
            elements = resize();
        }
        if (isEmpty()) {
            elements[tail] = element;
        } else  {
            elements[++tail] = element;
        }
        size++;
    }

    @Override
    public void push(E element) {
        addFirst(element);
    }

    @Override
    public void insert(int index, E element) {
        int realIndex = head + index;
        ensureIndex(realIndex);
        if (realIndex - head < tail - realIndex) {
            insertAndShiftLeft(realIndex - 1, element);
        } else {
            insertAndShiftRight(realIndex, element);
        }
    }

    private void insertAndShiftRight(int index, E element) {
        E lastElement = getAt(tail);
        for (int i = tail; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        addLast(lastElement);
        elements[index] = element;
    }

    private void insertAndShiftLeft(int index, E element) {
        E firstElement = getAt(head);
        for (int i = head; i < index; i++) {
            elements[i] = elements[i + 1];
        }
        addFirst(firstElement);
        elements[index] = element;
    }

    @Override
    public void set(int index, E element) {
        int realIndex = head + index;
        ensureIndex(realIndex);
        elements[realIndex] = element;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return getAt(head);
    }

    @Override
    public E poll() {
        return removeFirst();
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) elements[index];
    }

    @Override
    public E get(int index) {
        int realIndex = head + index;
        ensureIndex(realIndex);
        return getAt(realIndex);
    }

    @Override
    public E get(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = head; i <= tail; i++) {
            if (elements[i].equals(object)) {
                return getAt(i);
            }
        }
        return null;
    }

    @Override
    public E remove(int index) {
        int realIndex = head + index;
        ensureIndex(realIndex);
        E element = getAt(realIndex);
        elements[realIndex] = null;
        for (int j = realIndex; j < tail; j++) {
            elements[j] = elements[j + 1];
        }
        removeLast();
        return element;
    }

    private void ensureIndex(int index) {
        if (index < head || index > tail) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public E remove(Object object) {
        if (isEmpty()) {
            return null;
        }
        for (int i = head; i <= tail; i++) {
            if (elements[i].equals(object)) {
                E element = getAt(i);
                elements[i] = null;
                for (int j = i; j < tail; j++) {
                    elements[j] = elements[j + 1];
                }
                removeLast();
                return element;
            }
        }
        return null;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E element = getAt(head);
        elements[head++] = null;
        size--;
        if (size == 0) {
            reorganizeHeadAndTail();
        }
        return element;
    }

    private void reorganizeHeadAndTail() {
        int middle = elements.length / 2;
        head = tail = middle;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        E element = getAt(tail);
        elements[tail--] = null;
        size--;
        if (size == 0) {
            reorganizeHeadAndTail();
        }
        return element;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int capacity() {
        return elements.length;
    }

    @Override
    public void trimToSize() {
        Object[] newElements = new Object[size];
        int index = 0;
        for (int i = head; i <= tail; i++) {
            newElements[index++] = elements[i];
        }
        elements = newElements;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = head;

            @Override
            public boolean hasNext() {
                return index <= tail;
            }

            @Override
            public E next() {
                return getAt(index++);
            }
        };
    }
}
