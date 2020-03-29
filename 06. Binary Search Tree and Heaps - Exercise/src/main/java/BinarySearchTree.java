import java.util.ArrayList;
import java.util.function.Consumer;

import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {

    private Node<E> root;

    private int count;

    public static class Node<E> {

        private E value;

        private Node<E> leftChild;

        private Node<E> rightChild;

	public Node(E value) {
            this.value = value;
        }

        public Node<E> getLeft() {
            return this.leftChild;
        }

        public Node<E> getRight() {
            return this.rightChild;
        }

        public E getValue() {
            return this.value;
        }
    }

    public BinarySearchTree() {
        root = null;
        count = 0;
    }

    public BinarySearchTree(Node<E> root) {
        copy(root);
    }

    private void copy(Node<E> node) {
        insert(node.value);
        copy(node.getLeft());
        copy(node.getRight());
    }

    private void foreachInOrderTraversal(Node<E> node, Consumer<E> consumer) {
        if (node == null) {
            return;
        }
        foreachInOrderTraversal(node.getLeft(), consumer);

        consumer.accept(node.value);

        foreachInOrderTraversal(node.getRight(), consumer);
    }
	
	public void eachInOrder(Consumer<E> consumer) {
        foreachInOrderTraversal(root, consumer);
    }

    public Node<E> getRoot() {
        return root;
    }

    private boolean isGreater(E element, E nodeValue) {
        return element.compareTo(nodeValue) > 0;
    }

    private boolean isLess(E element, E nodeValue) {
        return element.compareTo(nodeValue) < 0;
    }

    private boolean isEqual(E element, E nodeValue) {
        return element.compareTo(nodeValue) == 0;
    }

    private Node<E> insertRecursive(Node<E> node, E element) {
        if (node == null) {
            node = new Node<>(element);
            count++;
            return node;
        }

        if (isLess(element, node.getValue())) {
            node.leftChild = insertRecursive(node.getLeft(), element);
        } else if (isGreater(element, node.getValue())) {
            node.rightChild = insertRecursive(node.getRight(), element);
        }

        return node;
    }

    public void insert(E element) {
        root = insertRecursive(getRoot(), element);
    }

    private boolean containsRecursive(Node<E> node, E element) {
        if (node == null) {
            return false;
        }

        if (isEqual(element, node.getValue())) {
            return true;
        }
        if (isLess(element, node.getValue())) {
            return containsRecursive(node.getLeft(), element);
        }
        return containsRecursive(node.getRight(), element);
    }

    public boolean contains(E element) {
//        return containsRecursive(root, element);
        Node<E> current = root;

        while (current != null) {
            if (isEqual(element, current.getValue())) {
                return true;
            }

            if (isLess(element, current.getValue())) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }

        return false;
    }

    private BinarySearchTree<E> searchIterative(Node<E> rootNode, E element) {
        Node<E> current = rootNode;

        while (current != null) {
            if (current.getValue().equals(element)) {
                return new BinarySearchTree<>(current);
            }

            if (isLess(element, current.getValue())) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return null;
    }

    public BinarySearchTree<E> search(E element) {
        return searchIterative(root, element);
    }

    private void addElementInRangeRecursive(Node<E> node, List<E> result, E lower, E upper) {
        if (node == null) {
            return;
        }

        if (!isLess(node.getValue(), lower) && !isGreater(node.getValue(), upper)) {
            result.add(node.getValue());
        }

        addElementInRangeRecursive(node.getLeft(), result, lower, upper);
        addElementInRangeRecursive(node.getRight(), result, lower, upper);
    }

    public List<E> range(E lower, E upper) {
        List<E> result = new ArrayList<>();
        if (isLess(lower, upper)) {
            return result;
        }

        addElementInRangeRecursive(root, result, lower, upper);
        return result;
    }

    private void ensureNonEmpty() {
        if (root == null) {
            throw new IllegalArgumentException("The binary search tree is empty");
        }
    }

    private Node<E> deleteMinRecursive(Node<E> currentNode, Node<E> previous) {
        if (currentNode.getLeft() == null) {
            if (currentNode.getRight() != null) {
                currentNode = currentNode.getRight();
            } else {
                previous.leftChild = null;
                currentNode = null;
            }
            count--;
        } else {
            currentNode.leftChild = deleteMinRecursive(currentNode.getLeft(), currentNode);
        }
        return currentNode;
    }

    public void deleteMin() {
        ensureNonEmpty();
        if (root.getLeft() == null) {
            root = root.getRight();
        } else {
            root.leftChild = deleteMinRecursive(root.getLeft(), root);
        }
    }

    private Node<E> deleteMaxRecursive(Node<E> currentNode, Node<E> previous) {
        if (currentNode.getRight() == null) {
            if (currentNode.getLeft() != null) {
                currentNode = currentNode.getLeft();
            } else {
                previous.rightChild = null;
                currentNode = null;
            }
            count--;
        } else {
            currentNode.rightChild = deleteMaxRecursive(currentNode.getRight(), currentNode);
        }
        return currentNode;
    }

    public void deleteMax() {
        ensureNonEmpty();
        if (root.getRight() == null) {
            root = root.getLeft();
        } else {
            root.rightChild = deleteMaxRecursive(root.getRight(), root);
        }
    }

    public int count() {
        return count;
    }

    private void findRankRecursive(Node<E> node, E element, int[] smallerElementsCount) {
        if (node == null) {
            return;
        }

        findRankRecursive(node.getLeft(), element, smallerElementsCount);

        if (isLess(node.getValue(), element)) {
            smallerElementsCount[0]++;
        } else {
            return;
        }

        findRankRecursive(node.getRight(), element, smallerElementsCount);
    }

    public int rank(E element) {
        int[] smallerElementsCount = { 0 };
        findRankRecursive(root, element, smallerElementsCount);
        return smallerElementsCount[0];
    }

    private void findCeilRecursive(Node<E> node, E element, List<E> ceilResult) {
        if (node == null) {
            return;
        }

        findCeilRecursive(node.getRight(), element, ceilResult);

        if (isGreater(node.getValue(), element)) {
            ceilResult.set(0, node.getValue());
        } else {
            return;
        }

        findCeilRecursive(node.getLeft(), element, ceilResult);
    }

    public E ceil(E element) {
        List<E> ceilResult = new ArrayList<>();
        ceilResult.add(null);
        findCeilRecursive(root, element, ceilResult);
        return ceilResult.get(0);
    }

    private void findFloorRecursive(Node<E> node, E element, List<E> floorResult) {
        if (node == null) {
            return;
        }

        findFloorRecursive(node.getLeft(), element, floorResult);
        if (isLess(node.getValue(), element)) {
            floorResult.set(0, node.getValue());
        } else {
            return;
        }
        findFloorRecursive(node.getRight(), element, floorResult);
    }

    public E floor(E element) {
        List<E> floorResult = new ArrayList<>();
        floorResult.add(null);
        findFloorRecursive(root, element, floorResult);
        return floorResult.get(0);
    }
}
