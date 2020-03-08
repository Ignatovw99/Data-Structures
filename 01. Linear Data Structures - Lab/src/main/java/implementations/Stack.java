package implementations;

import interfaces.AbstractStack;

import java.util.Iterator;

public class Stack<E> implements AbstractStack<E> {

    private static class Node<E> {

        private E value;

        private Node<E> next;

        public Node(E value) {
            this.value = value;
        }
    }

    private Node<E> top;

    private int size;

    public Stack() {
        top = null;
        size = 0;
    }

    @Override
    public void push(E element) {
        Node<E> nodeToInsert = new Node<>(element);

        if (top == null) {
            top = nodeToInsert;
        } else {
            nodeToInsert.next = top;
            top = nodeToInsert;
        }

        size++;
    }

    @Override
    public E pop() {
        ensureNonEmpty();

        Node<E> oldTop = top;
        top = oldTop.next;

        size--;

        return oldTop.value;
    }

    @Override
    public E peek() {
        ensureNonEmpty();

        return top.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {

            private Node<E> current = top;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E currentValue = current.value;
                current = current.next;
                return currentValue;
            }
        };
    }

    private void ensureNonEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
