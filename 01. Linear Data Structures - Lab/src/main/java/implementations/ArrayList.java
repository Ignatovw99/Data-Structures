package implementations;

import interfaces.List;

import java.util.Iterator;

public class ArrayList<E> implements List<E> {

    private static final int INITIAL_SIZE = 4;

    private Object[] elements;

    private int size;

    private int capacity;

    public ArrayList() {
        elements = new Object[INITIAL_SIZE];
        size = 0;
        capacity = INITIAL_SIZE;
    }

    @Override
    public boolean add(E element) {
        if (size == capacity) {
            elements = grow();
        }
        elements[size++] = element;
        return true;
    }

    @Override
    public boolean add(int index, E element) {
        if (!checkIndex(index)) {
            return false;
        }
        if (size == capacity) {
            elements = grow();
        }
        shiftRight(index);
        elements[index] = element;
        size++;

        return true;
    }

    @Override
    public E get(int index) {
        ensureIndex(index);
        return (E) elements[index];
    }

    @Override
    public E set(int index, E element) {
        ensureIndex(index);
        Object oldElement = elements[index];
        elements[index] = element;
        return (E) oldElement;
    }

    @Override
    public E remove(int index) {
        ensureIndex(index);
        Object removedElement = elements[index];
        shiftLeft(index);
        size--;
        if (size < capacity / 3) {
            shrink();
        }
        return (E) removedElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int indexOf(E element) {
        for (int i = 0; i < size; i++) {
            if (elements[i].equals(element)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean contains(E element) {
        return indexOf(element) != -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                return get(index++);
            }
        };
    }

    private Object[] grow() {
        capacity *= 2;
        Object[] copy = new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        return copy;
    }
    
    private void shiftLeft(int index) {
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
    }
    
    private void shiftRight(int index) {
        for (int i = size - 1; i >= index; i--) {
            elements[i + 1] = elements[i];
        }
    }

    private boolean checkIndex(int index) {
        return index >= 0 && index < size;
    }

    private void ensureIndex(int index) {
        if (!checkIndex(index)) {
            throw new IndexOutOfBoundsException("Cannot get index " + index + " on ArrayList with " + size + " elements.");
        }
    }

    private void shrink() {
        capacity /= 2;
        Object[] temp = new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = elements[i];
        }
        elements = temp;
    }
}
