package implementations;

import interfaces.AbstractQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PriorityQueue<E extends Comparable<E>> implements AbstractQueue<E> {

    private List<E> elements;

    public PriorityQueue() {
        elements = new ArrayList<>();
    }

    private void ensureNonEmpty() {
        if (size() == 0) {
            throw new IllegalStateException("Heap is empty upon peek/poll attempt");
        }
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public void add(E element) {
        elements.add(element);
        heapifyUp(size() - 1);
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private boolean isParentLessThanChild(E parent, E child) {
        return isFirstLessThanSecond(parent, child);
    }

    private E getParent(int childIndex) {
        return getAt(getParentIndex(childIndex));
    }

    private void heapifyUp(int index) {
        while (hasParent(index) && isParentLessThanChild(getParent(index), getAt(index))) {
            Collections.swap(elements, getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return elements.get(0);
    }

    @Override
    public E poll() {
        ensureNonEmpty();
        E returnedValue = getAt(0);

        Collections.swap(elements, 0, size() - 1);
        elements.remove(size() - 1);
        heapifyDown(0);

        return returnedValue;
    }

    private boolean isFirstLessThanSecond(E first, E second) {
        return first.compareTo(second) < 0;
    }

    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int getLeftChildIndex(int parenIndex) {
        return 2 * parenIndex + 1;
    }

    private int getRightChildIndex(int parenIndex) {
        return 2 * parenIndex + 2;
    }

    private E getLeftChild(int parentIndex) {
        return getAt(getLeftChildIndex(parentIndex));
    }

    private E getRightChild(int parentIndex) {
        return getAt(getRightChildIndex(parentIndex));
    }

    private void heapifyDown(int index) {
        while (index < size() / 2) {
            int childIndex = getLeftChildIndex(index);

            // Check if the right child is greater or equal than left child
            if (getRightChildIndex(index) < size() && isFirstLessThanSecond(getAt(childIndex), getAt(getRightChildIndex(index)))) {
                childIndex = childIndex + 1;
            }

            //Check if the parent is greater or equal than his child
            if (isFirstLessThanSecond(getAt(childIndex), getAt(index))) {
                break;
            }
            Collections.swap(elements, index, childIndex);

            index = childIndex;
        }
    }

    private E getAt(int index) {
        return elements.get(index);
    }
}
