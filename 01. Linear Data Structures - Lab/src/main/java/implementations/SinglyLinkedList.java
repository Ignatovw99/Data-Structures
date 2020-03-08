package implementations;

import interfaces.LinkedList;

import java.util.Iterator;

public class SinglyLinkedList<E> implements LinkedList<E> {

    private static class Node<E> {

        private E value;

        private Node<E> next;

        public Node(E value) {
            this.value = value;
        }
    }

    private Node<E> head;

    private int size;

    public SinglyLinkedList() {
        head = null;
        size = 0;
    }

    @Override
    public void addFirst(E element) {
        Node<E> nodeToInsert = new Node<>(element);

        nodeToInsert.next = head;
        head = nodeToInsert;

        size++;
    }

    @Override
    public void addLast(E element) {
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
    public E removeFirst() {
        ensureNonEmpty();

        Node<E> nodeToRemove = head;
        head = nodeToRemove.next;
        size--;

        return nodeToRemove.value;
    }

    @Override
    public E removeLast() {
        ensureNonEmpty();

        E removedValue;

        if (size == 1) {
            removedValue = head.value;
            head = null;
        } else {
            Node<E> preLastNode = head;
            Node<E> nodeToRemove = preLastNode.next;

            while (nodeToRemove.next != null) {
                preLastNode = nodeToRemove;
                nodeToRemove = nodeToRemove.next;
            }

            removedValue = nodeToRemove.value;
            preLastNode.next = null;
        }
        size--;
        return removedValue;
    }

    @Override
    public E getFirst() {
        ensureNonEmpty();
        return head.value;
    }

    @Override
    public E getLast() {
        ensureNonEmpty();

        Node<E> current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current.value;
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
