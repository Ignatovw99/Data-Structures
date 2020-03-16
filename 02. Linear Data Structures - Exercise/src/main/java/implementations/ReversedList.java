package implementations;

import java.util.Iterator;

public class ReversedList<E> implements interfaces.ReversedList<E> {

    private final int INITIAL_CAPACITY = 2;

    private Object[] elements;

    private int head;

    private int size;

    public ReversedList() {
        elements = new Object[INITIAL_CAPACITY];
        head = 0;
        size = 0;
    }

    private Object[] resize() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        for (int i = 0; i < head; i++) {
            newElements[i] = elements[i];
        }
        return newElements;
    }

    @SuppressWarnings("unchecked")
    private E getAt(int index) {
        return (E) elements[index];
    }

    private void ensureIndex(int index) {
        if (size == 0 || index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public void add(E element) {
        if (size == capacity()) {
            elements = resize();
        }
        elements[head++] = element;
        size++;
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
    public E get(int index) {
        int actualIndex = head - index - 1;
        ensureIndex(actualIndex);
        return getAt(actualIndex);
    }

    @Override
    public void removeAt(int index) {
        ensureIndex(index);
        for (int i = index; i < capacity() - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--head] = null;
        size--;
    }

    @Override
    public Iterator<E> iterator() {

        return new Iterator<E>() {

            private int index = head - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public E next() {
                return getAt(index--);
            }
        };
    }
}
