import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {

    public static class Node<T> {
        private T key;
        private Node<T> left, right;
        private int level;
        private int count;

        public Node(T key) {
            this.key = key;
            level = 1;
            count = 1;
        }
    }

    private Node<T> root;

    public AATree() {

    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    public void insert(T element) {
        root = insert(root, element);
    }

    private Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }

        int compareResult = element.compareTo(node.key);

        if (compareResult < 0) {
            node.left = insert(node.left, element);
        } else if (compareResult > 0) {
            node.right = insert(node.right, element);
        }

        node = skew(node);
        node = split(node);

        node.count = getCountNodesOf(node.left) + getCountNodesOf(node.right) + 1;

        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left == null) {
            return node;
        }
        if (node.level == node.left.level) {
            Node<T> result = node.left;
            node.left = result.right;
            result.right = node;

            return result;
        }

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right == null || node.right.right == null || node.level != node.right.right.level) {
            return node;
        }

        Node<T> result = node.right;
        node.right = result.left;
        result.left = node;

        result.level++;
        node.count = getCountNodesOf(node.left) + 1;
        return result;
    }

    public int countNodes() {
        return getCountNodesOf(root);
    }

    private int getCountNodesOf(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    public boolean search(T element) {
        Node<T> current = root;

        while (current != null) {
            int compareResult = element.compareTo(current.key);
            if (compareResult < 0) {
                current = current.left;
            } else if (compareResult > 0) {
                current = current.right;
            } else {
                return true;
            }
        }

        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        inOrderRecursive(root, consumer);
    }

    private void inOrderRecursive(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        inOrderRecursive(node.left, consumer);
        consumer.accept(node.key);
        inOrderRecursive(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        preOrderRecursive(root, consumer);
    }

    private void preOrderRecursive(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }
        consumer.accept(node.key);
        preOrderRecursive(node.left, consumer);
        preOrderRecursive(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        postOrderRecursive(root, consumer);
    }

    private void postOrderRecursive(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }
        postOrderRecursive(node.left, consumer);
        postOrderRecursive(node.right, consumer);
        consumer.accept(node.key);
    }
}