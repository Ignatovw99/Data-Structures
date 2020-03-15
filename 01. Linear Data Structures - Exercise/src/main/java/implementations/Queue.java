package implementations;

import interfaces.AbstractQueue;

import java.util.Iterator;

//When there are head and tail in the queue, the offer and poll operation are with O(1) complexity,
//Otherwise with one and only head offer has O(n) complexity
public class Queue<E> implements AbstractQueue<E> {

    private Node<E> head;

    private Node<E> tail;

    private int size;

    private static class Node<E> {

        private E element;

        private Node<E> next;

        private Node(E element) {
            this.element = element;
        }
    }

    public Queue() {
    }

    @Override
    public void offer(E element) {
        Node<E> newNode = new Node<>(element);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        this.size++;
    }

    @Override
    public E poll() {
        ensureNonEmpty();
        E element = head.element;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
        }
        size--;
        return element;
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return head.element;
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
                E element = current.element;
                current = current.next;
                return element;
            }
        };
    }

    private void ensureNonEmpty() {
        if (size == 0) {
            throw new IllegalStateException("Illegal operation on empty stack");
        }
    }
}
