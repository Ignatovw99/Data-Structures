import java.util.ArrayDeque;
import java.util.Deque;

public class RedBlackTree<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    public RedBlackTree() {
    }

    // is node red; false if node is null ?
    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        return node.color;
    }

    // number of node in subtree rooted at node; 0 if node is null
    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return size(root);
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    public Value get(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null in get() call.");
        }

        Node current = root;

        while (current != null) {
            int compareResult = key.compareTo(current.key);
            if (compareResult < 0) {
                current = current.left;
            } else if (compareResult > 0) {
                current = current.right;
            } else {
                return current.val;
            }
        }

        return null;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    public void put(Key key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null on put() call.");
        }
        root = put(root, key, val);
        root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at node
    private Node put(Node node, Key key, Value val) {
        if (node == null) {
            return new Node(key, val, RED, 1);
        }

        int compareResult = key.compareTo(node.key);

        if (compareResult < 0) {
            node.left = put(node.left, key, val);
        } else if (compareResult > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
        }

        node = balance(node);

        return node;
    }

    public void deleteMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Tree is empty.");
        }
        if (root.left == null) {
            root = null;
            return;
        }
        root = deleteMin(root);
    }

    // delete the key-value pair with the minimum key rooted at node
    private Node deleteMin(Node node) {
        if (node.left == null) {
            return null;
        }

        if (!isRed(node.left) && !isRed(node.left.left)) {
            node = moveRedLeft(node);
        }

        node.left = deleteMin(node.left);
        return balance(node);
    }

    public void deleteMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Tree is empty.");
        }
        if (root.right == null) {
            root = null;
            return;
        }
        root = deleteMax(root);
    }

    // delete the key-value pair with the maximum key rooted at node
    private Node deleteMax(Node node) {
        if (isRed(node.left)) {
            node = rotateRight(node);
        }

        if (node.right == null) {
            return null;
        }

        if (!isRed(node.right) && !isRed(node.right.right)) {
            node = moveRedRight(node);
        }

        node.right = deleteMax(node.right);
        return balance(node);
    }

    public void delete(Key key) {
        if (key == null || isEmpty()) {
            throw new IllegalStateException("Delete call within illegal state.");
        }
//        if (!contains(key)) {
//            return;
//        }
        root = delete(root, key);

        if (!isEmpty()) {
            root.color = BLACK;
        }
    }

    // delete the key-value pair with the given key rooted at node
    private Node delete(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.key);

        if (compareResult < 0) {
            if (!isRed(node.left) && !isRed(node.left.left)) {
                node = moveRedLeft(node);
            }
            node.left = delete(node.left, key);
        } else {
            if (isRed(node.left)) {
                node = rotateRight(node);
            }

            if (compareResult > 0) {
                node.right = delete(node.right, key);
            } else {
                if (node.right == null) {
                    return null;
                }
                if (!isRed(node.right) && !isRed(node.right.right)) {
                    node = moveRedRight(node);
                }

                Node min = min(node.right);
                node.key = min.key;
                node.val = min.val;
                node.right = deleteMin(node.right);
            }
        }

        return balance(node);
    }

    private Node rotateRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;

        temp.color = node.color;
        node.color = RED;

        temp.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;

        return temp;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;

        temp.color = node.color;
        node.color = RED;

        temp.size = node.size;
        node.size = size(node.left) + size(node.right) + 1;

        return temp;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    // Assuming that node is red and both node.left and node.left.left
    // are black, make node.left or one of its children red.
    private Node moveRedLeft(Node node) {
        flipColors(node);
        if (isRed(node.right.left)) {
            node.right = rotateRight(node.right);
            node = rotateLeft(node);
            flipColors(node);
        }
        return node;
    }

    // Assuming that node is red and both node.right and node.right.left
    // are black, make node.right or one of its children red.
    private Node moveRedRight(Node node) {
        flipColors(node);
        if (isRed(node.left.left)) {
            node = rotateRight(node);
            flipColors(node);
        }
        return node;
    }

    // restore red-black tree invariant
    private Node balance(Node node) {
        if (isRed(node.right)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        node.size = size(node.left) + size(node.right) + 1;

        return node;
    }

    public int height() {
        return height(root);
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    public Key min() {
        Node minNode = min(root);
        return minNode != null ? minNode.key : null;
    }

    // the smallest key in subtree rooted at node; null if no such key
    private Node min(Node node) {
        while (node != null && node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Key max() {
        Node maxNode = max(root);
        return maxNode == null ? null : maxNode.key;
    }

    // the largest key in the subtree rooted at node; null if no such key
    private Node max(Node node) {
        if (node.right == null) {
            return node;
        }
        return max(node.right);
    }

    public Key floor(Key key) {
        Node floor = floor(root, key);
        if (floor == null) {
            throw new IllegalArgumentException();
        }
        return floor.key;
    }

    // the largest key in the subtree rooted at node less than or equal to the given key
    private Node floor(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.key);

        if (compareResult == 0) {
            return node;
        }
        if (compareResult < 0) {
            return floor(node.left, key);
        }

        Node rightFloor = floor(node.right, key);
        if (rightFloor != null) {
            return rightFloor;
        }

        return node;
    }

    public Key ceiling(Key key) {
        Node ceiling = ceiling(root, key);
        if (ceiling == null) {
            throw new IllegalArgumentException();
        }
        return ceiling.key;
    }

    // the smallest key in the subtree rooted at node greater than or equal to the given key
    private Node ceiling(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.key);

        if (compareResult == 0) {
            return node;
        }
        if (compareResult > 0) {
            return ceiling(node.right, key);
        }

        Node leftCeiling = ceiling(node.left, key);
        if (leftCeiling != null) {
            return leftCeiling;
        }

        return node;
    }

    public Key select(int rank) {
        return select(root, rank);
    }

    // Return key in BST rooted at node of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node node, int rank) {
        if (node == null) {
            return null;
        }

        int leftSize = size(node.left);

        if (leftSize > rank) {
            return select(node.left, rank);
        } else if (leftSize < rank) {
            return select(node.right, rank - leftSize - 1);
        } else {
            return node.key;
        }
    }

    public int rank(Key key) {
        return rank(root, key);
    }

    // number of keys less than key in the subtree rooted at node
    private int rank(Node node, Key key) {
        if (node == null) {
            return 0;
        }

        int compareResult = key.compareTo(node.key);

        if (compareResult < 0) {
            return rank(node.left, key);
        } else if (compareResult > 0) {
            return rank(node.right, key) + size(node.left) + 1;
        } else {
            return size(node.left);
        }
    }

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        Deque<Key> deque = new ArrayDeque<>();
        keys(root, deque, lo, hi);
        return deque;
    }

    // add the keys between lo and hi in the subtree rooted at node
    // to the queue
    private void keys(Node node, Deque<Key> queue, Key lo, Key hi) {
        if (node == null) {
            return;
        }

        int lowerCompare = lo.compareTo(node.key);
        int upperCompare = hi.compareTo(node.key);

        if (lowerCompare < 0) {
            keys(node.left, queue, lo, hi);
        }
        if (lowerCompare <= 0 && upperCompare >= 0) {
            queue.offer(node.key);
        }
        if (upperCompare > 0) {
            keys(node.right, queue, lo, hi);
        }
    }

    public int size(Key lo, Key hi) {
        int lowerAndUpperCompare = lo.compareTo(hi);

        if (lowerAndUpperCompare > 0) {
            return 0;
        }

        return rank(hi) - rank(lo);
    }
}