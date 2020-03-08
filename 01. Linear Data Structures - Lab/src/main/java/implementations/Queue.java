package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

public class Queue<E> implements AbstractQueue<E> {

    private static class Node<E> {

        private E value;

        private Node<E> next;

        public Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;

    private int size;

    public Queue() {
        head = null;
        size = 0;
    }

    @Override
    public void offer(E element) {
        Node<E> nodeToInsert = new Node<>(element);
        if (isEmpty()) {
            head = nodeToInsert;
        } else {
            Node<E> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = nodeToInsert;
        }
        size++;
    }

    @Override
    public E poll() {
        ensureNonEmpty();

        Node<E> oldHead = head;
        head = oldHead.next;

        size--;
        return oldHead.value;
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return head.value;
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
        return new Iterator<E>() {

            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E value = current.value;
                current = current.next;
                return value;
            }
        };
    }

    private void ensureNonEmpty() {
        if (isEmpty()) {
            throw new IllegalStateException();
        }
    }
}
