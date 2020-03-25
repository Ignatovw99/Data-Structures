package implementations;

import interfaces.AbstractBinarySearchTree;

public class BinarySearchTree<E extends Comparable<E>> implements AbstractBinarySearchTree<E> {

    private Node<E> root;

    public BinarySearchTree() {
    }

    public BinarySearchTree(Node<E> root) {
        copy(root);
    }

    private void copy(Node<E> node) {
        if (node == null) {
            return;
        }

        insert(node.value);
        copy(node.leftChild);
        copy(node.rightChild);
    }

    @Override
    public void insert(E element) {
        root = insertNode(root, element);
    }

    private Node<E> insertNode(Node<E> node, E element) {
        if (node == null) {
            node = new Node<>(element);
            return node;
        }

        if (isLess(element, node.value)) {
            node.leftChild = insertNode(node.leftChild, element);
        } else if (isGreater(element, node.value)) {
            node.rightChild = insertNode(node.rightChild, element);
        }

        return node;
    }

    private boolean isGreater(E element, E nodeValue) {
        return element.compareTo(nodeValue) > 0;
    }

    private boolean isLess(E element, E nodeValue) {
        return element.compareTo(nodeValue) < 0;
    }

    @Override
    public boolean contains(E element) {
        // Iterable solution
        //       |
        //       V

//        Node<E> current = root;
//
//        while (current != null) {
//            if (current.value.equals(element)) {
//                return true;
//            } else if (isLess(element, current.value)) {
//                current = current.leftChild;
//            } else {
//                current = current.rightChild;
//            }
//        }
//
//        return false;
        return containsRecursive(root, element);
    }

    private boolean containsRecursive(Node<E> node, E element) {
        if (node == null) {
            return false;
        }
        if (node.value.equals(element)) {
            return true;
        }

        if (isLess(element, node.value)) {
            return containsRecursive(node.leftChild, element);
        } else {
            return containsRecursive(node.rightChild, element);
        }
    }

    @Override
    public AbstractBinarySearchTree<E> search(E element) {
        return searchRecursive(getRoot(), element);
    }

    private AbstractBinarySearchTree<E> searchRecursive(Node<E> node, E element) {
        if (node == null) {
            return null;
        }
        if (node.value.equals(element)) {
            return new BinarySearchTree<>(node);
        }

        if (isLess(element, node.value)) {
            return searchRecursive(node.leftChild, element);
        } else {
            return searchRecursive(node.rightChild, element);
        }
    }

    @Override
    public Node<E> getRoot() {
        return root;
    }

    @Override
    public Node<E> getLeft() {
        return root.leftChild;
    }

    @Override
    public Node<E> getRight() {
        return root.rightChild;
    }

    @Override
    public E getValue() {
        return root.value;
    }
}
