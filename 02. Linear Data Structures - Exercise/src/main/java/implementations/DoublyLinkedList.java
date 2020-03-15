package implementations;

import interfaces.LinkedList;

import java.util.Iterator;

public class DoublyLinkedList<E> implements LinkedList<E> {

    private Node<E> head;

    private Node<E> tail;

    private int size;

    private static class Node<E> {

        private E element;

        private Node<E> next;

        private Node<E> previous;

        public Node(E value) {
            element = value;
        }
    }

    public DoublyLinkedList() {
    }

    @Override
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (size == 0) {
            head = tail = newNode;
        } else {
            head.previous = newNode;
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    @Override
    public void addLast(E element) {
        if (size == 0) {
            addFirst(element);
        } else {
            Node<E> newNode = new Node<>(element);
            newNode.previous = tail;
            tail.next = newNode;
            tail = newNode;
            size++;
        }
    }

    @Override
    public E removeFirst() {
        ensureNotEmpty();
        E element = head.element;
        if (size == 1) {
            head = tail = null;
        } else {
            head = head.next;
            head.previous = null;
        }
        size--;
        return element;
    }

    private void ensureNotEmpty() {
        if (size == 0) {
            throw new IllegalStateException("Illegal remove for empty LinkedList");
        }
    }

    @Override
    public E removeLast() {
        ensureNotEmpty();
        E element;
        if (size == 1) {
            element = removeFirst();
        } else {
            element = tail.element;
            tail = tail.previous;
            tail.next = null;
            size--;
        }

        return element;
    }

    @Override
    public E getFirst() {
        ensureNotEmpty();
        return head.element;
    }

    @Override
    public E getLast() {
        ensureNotEmpty();
        return tail.element;
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
}
