package solutions;

import interfaces.Decrease;
import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap<E extends Comparable<E> & Decrease<E>> implements Heap<E> {

    private List<E> elements;

    public MinHeap() {
        elements = new ArrayList<>();
    }

    @Override
    public int size() {
        return elements.size();
    }

    private E getParent(int childIndex) {
        return getAt(getParentIndex(childIndex));
    }

    private E getAt(int index) {
        return elements.get(index);
    }

    private boolean isParentGreaterThanChild(E parent, E child) {
        return parent.compareTo(child) > 0;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private void heapifyUp(int index) {
        while (hasParent(index) && isParentGreaterThanChild(getParent(index), getAt(index))) {
            Collections.swap(elements, getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    @Override
    public void add(E element) {
        elements.add(element);
        heapifyUp(size() - 1);
    }

    private void ensureNonEmpty() {
        if (size() == 0) {
            throw new IllegalStateException("Heap is empty upon peek/poll attempt");
        }
    }

    @Override
    public E peek() {
        ensureNonEmpty();
        return getAt(0);
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

    private int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    private void heapifyDown(int index) {
        while (index < size() / 2) {
            int childIndex = getLeftChildIndex(index);

            if (getRightChildIndex(index) < size() && isRightChildLessThanLeftChild(getAt(getRightChildIndex(index)), getAt(childIndex))) {
                childIndex = getRightChildIndex(index);
            }

            if (isParentLessThanChild(getAt(index), getAt(childIndex))) {
                break;
            }

            Collections.swap(elements, index, childIndex);
            index = childIndex;
        }
    }

    private boolean isParentLessThanChild(E parent, E child) {
        return parent.compareTo(child) < 0;
    }

    private boolean isRightChildLessThanLeftChild(E rightChild, E leftChild) {
        return rightChild.compareTo(leftChild) < 0;
    }

    @Override
    public void decrease(E element) {
        int elementIndex = elements.indexOf(element);
        E heapElement = elements.get(elementIndex);
        heapElement.decrease();
        heapifyUp(elementIndex);
    }
}
