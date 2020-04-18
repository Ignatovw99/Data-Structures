package main;

import java.util.*;
import java.util.stream.Collectors;

public class Hierarchy<T> implements IHierarchy<T> {

    private Map<T, HierarchyNode<T>> data;

    private HierarchyNode<T> root;

    public Hierarchy(T element) {
        data = new HashMap<>();
        root = new HierarchyNode<>(element);
        data.put(element, root);
    }

    private HierarchyNode<T> ensureExistsAndGet(T key) {
        HierarchyNode<T> node = data.get(key);

        if (node == null) {
            throw new IllegalArgumentException();
        }

        return node;
    }

    @Override
    public int getCount() {
        //Delegate to map
        return data.size();
    }

    @Override
    public void add(T element, T child) {
        HierarchyNode<T> parent = ensureExistsAndGet(element);;

        if (data.containsKey(child)) {
            throw new IllegalArgumentException();
        }

        HierarchyNode<T> childToBeAdded = new HierarchyNode<>(child);

        childToBeAdded.setParent(parent);
        parent.getChildren().add(childToBeAdded);

        data.put(child, childToBeAdded);
    }

    @Override
    public void remove(T element) {
        HierarchyNode<T> nodeToRemove = ensureExistsAndGet(element);

        if (nodeToRemove.getParent() == null) {
            throw new IllegalStateException();
        }

        HierarchyNode<T> parent = nodeToRemove.getParent();

        List<HierarchyNode<T>> children = nodeToRemove.getChildren();

        parent.getChildren().addAll(children);
        parent.getChildren().remove(nodeToRemove);
        children.forEach(child -> child.setParent(parent));

        data.remove(nodeToRemove.getValue());
    }

    @Override
    public Iterable<T> getChildren(T element) {
        HierarchyNode<T> node = ensureExistsAndGet(element);
        return node.getChildren()
                .stream()
                .map(HierarchyNode::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public T getParent(T element) {
        HierarchyNode<T> node = ensureExistsAndGet(element);

        return node.getParent() == null ? null : node.getParent().getValue();
    }

    @Override
    public boolean contains(T element) {
        return data.containsKey(element);
    }

    @Override
    public Iterable<T> getCommonElements(IHierarchy<T> other) {
        List<T> commonElements = new ArrayList<>();

        data.keySet().forEach(key -> {
            if (other.contains(key)) {
                commonElements.add(key);
            }
        });

        return commonElements;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            Deque<HierarchyNode<T>> deque = new ArrayDeque<>(Collections.singletonList(root));

            @Override
            public boolean hasNext() {
                return !deque.isEmpty();
            }

            @Override
            public T next() {
                HierarchyNode<T> nextNode = deque.poll();
                if (nextNode == null) {
                    return null;
                }
                deque.addAll(nextNode.getChildren());
                return nextNode.getValue();
            }
        };
    }
}
