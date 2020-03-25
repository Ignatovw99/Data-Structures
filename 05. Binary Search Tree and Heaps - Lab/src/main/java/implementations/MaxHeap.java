package implementations;

import interfaces.Heap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxHeap<E extends Comparable<E>> implements Heap<E> {

    private List<E> elements;

    public MaxHeap() {
        this.elements = new ArrayList<>();
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

    private E getAt(int index) {
        return elements.get(index);
    }

    private boolean isParentLessThanChild(E parent, E child) {
        return parent.compareTo(child) < 0;
    }

    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private E getParent(int childIndex) {
        return getAt(getParentIndex(childIndex));
    }

    //Bottom up approach
    private void heapifyUp(int index) {
        while (hasParent(index) && isParentLessThanChild(getParent(index), getAt(index))) {
            Collections.swap(elements, getParentIndex(index), index);
            index = getParentIndex(index);
        }
    }

    @Override
    public E peek() {
        if (size() == 0) {
            throw new IllegalStateException("Heap is empty upon peek attempt");
        }
        return getAt(0);
    }
}
