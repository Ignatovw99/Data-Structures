package core;

import model.Message;
import shared.DataTransferSystem;

import java.util.ArrayList;
import java.util.List;

public class MessagingSystem implements DataTransferSystem {

    class Node {

        public Message message;

        public Node leftChild;

        public Node rightChild;

        public Node(Message message) {
            this.message = message;
        }
    }

    private Node root;

    private int size;

    public MessagingSystem() {
        root = null;
        size = 0;
    }

    private boolean isEqual(int first, int second) {
        return first == second;
    }

    private boolean isGreater(int first, int second) {
        return first > second;
    }

    private boolean isLess(int first, int second) {
        return first < second;
    }

    private Node addMessageRecursive(Node node, Message message) {
        if (node == null) {
            node = new Node(message);
            size++;
            return node;
        }

        if (isLess(message.getWeight(), node.message.getWeight())) {
            node.leftChild = addMessageRecursive(node.leftChild, message);
        } else if (isGreater(message.getWeight(), node.message.getWeight())) {
            node.rightChild = addMessageRecursive(node.rightChild, message);
        } else if (isEqual(message.getWeight(), node.message.getWeight())) {
            throw new IllegalArgumentException();
        }

        return node;
    }

    @Override
    public void add(Message message) {
        root = addMessageRecursive(root, message);
    }

    private Node findNodeByWeight(Node node, int weight) {
        if (node == null) {
            return null;
        }
        if (node.message.getWeight() == weight) {
            return node;
        }

        if (isLess(weight, node.message.getWeight())) {
            return findNodeByWeight(node.leftChild, weight);
        } else {
            return findNodeByWeight(node.rightChild, weight);
        }
    }

    @Override
    public Message getByWeight(int weight) {
        Node found = findNodeByWeight(root, weight);
        if (found == null) {
            throw new IllegalArgumentException();
        }
        return found.message;
    }

    private void getMinRecursive(Node currentNode, Node[] lightest) {
        if (currentNode.leftChild == null) {
            lightest[0] = currentNode;
        } else {
            getMinRecursive(currentNode.leftChild, lightest);
        }
    }

    @Override
    public Message getLightest() {
        ensureNonEmpty();
        Node[] lightest = {new Node(null)};
        if (root.leftChild == null) {
            lightest[0] = root;
        } else {
            getMinRecursive(root.leftChild, lightest);
        }
        return lightest[0].message;
    }

    private void getMaxRecursive(Node currentNode, Node[] heaviest) {
        if (currentNode.rightChild == null) {
            heaviest[0] = currentNode;
        } else {
            getMaxRecursive(currentNode.rightChild, heaviest);
        }
    }

    @Override
    public Message getHeaviest() {
        ensureNonEmpty();
        Node[] heaviest = {new Node(null)};
        if (root.rightChild == null) {
            heaviest[0] = root;
        } else {
            getMaxRecursive(root.rightChild, heaviest);
        }
        return heaviest[0].message;
    }

    @Override
    public Message deleteLightest() {
        ensureNonEmpty();
        Node[] deleted = {new Node(null)};
        if (root.leftChild == null) {
            deleted[0] = root;
            root = root.rightChild;
        } else {
            root.leftChild = deleteMinRecursive(root.leftChild, root, deleted);
        }
        size--;
        return deleted[0].message;
    }

    private void ensureNonEmpty() {
        if (size == 0) {
            throw new IllegalStateException();
        }
    }

    private Node deleteMinRecursive(Node currentNode, Node parent, Node[] deleted) {
        if (currentNode.leftChild == null) {
            if (currentNode.rightChild != null) {
                deleted[0] = currentNode;
                currentNode = currentNode.rightChild;
            } else {
                deleted[0] = currentNode;
                parent.leftChild = null;
                currentNode = null;
            }
        } else {
            currentNode.leftChild = deleteMinRecursive(currentNode.leftChild, currentNode, deleted);
        }
        return currentNode;
    }

    @Override
    public Message deleteHeaviest() {
        ensureNonEmpty();
        Node[] deleted = {new Node(null)};
        if (root.rightChild == null) {
            deleted[0] = root;
            root = root.leftChild;
        } else {
            root.rightChild = deleteMaxRecursive(root.rightChild, root, deleted);
        }
        size--;
        return deleted[0].message;
    }

    private Node deleteMaxRecursive(Node currentNode, Node parent, Node[] deleted) {
        if (currentNode.rightChild == null) {
            if (currentNode.leftChild != null) {
                deleted[0] = currentNode;
                currentNode = currentNode.leftChild;
            } else {
                deleted[0] = currentNode;
                parent.rightChild = null;
                currentNode = null;
            }
        } else {
            currentNode.rightChild = deleteMaxRecursive(currentNode.rightChild, currentNode, deleted);
        }
        return currentNode;
    }

    @Override
    public Boolean contains(Message message) {
        return containsRecursive(root, message);
    }

    private Boolean containsRecursive(Node node, Message message) {
        if (node == null) {
            return false;
        }
        if (node.message.equals(message)) {
            return true;
        }

        if (isLess(message.getWeight(), node.message.getWeight())) {
            return containsRecursive(node.leftChild, message);
        } else {
            return containsRecursive(node.rightChild, message);
        }
    }

    @Override
    public List<Message> getOrderedByWeight() {
        return getInOrder();
    }

    @Override
    public List<Message> getPostOrder() {
        List<Message> postOrderResult = new ArrayList<>();
        if (size == 0) {
            return postOrderResult;
        }
        getPost(root, postOrderResult);

        return postOrderResult;
    }

    private void getPost(Node node, List<Message> postOrderResult) {
        if (node.leftChild != null) {
            getPost(node.leftChild, postOrderResult);
        }

        if (node.rightChild != null) {
            getPost(node.rightChild, postOrderResult);
        }

        postOrderResult.add(node.message);
    }

    @Override
    public List<Message> getPreOrder() {
        List<Message> preOrderResult = new ArrayList<>();

        if (size == 0) {
            return preOrderResult;
        }

        getPre(root, preOrderResult);

        return preOrderResult;
    }

    private void getPre(Node node, List<Message> preOrderResult) {
        preOrderResult.add(node.message);

        if (node.leftChild != null) {
            getPre(node.leftChild, preOrderResult);
        }

        if (node.rightChild != null) {
            getPre(node.rightChild, preOrderResult);
        }
    }

    @Override
    public List<Message> getInOrder() {
        List<Message> inOrderResult = new ArrayList<>();

        if (size==0) {
            return inOrderResult;
        }
        getIn(root, inOrderResult);

        return inOrderResult;
    }

    private void getIn(Node node, List<Message> inOrderResult) {

        if (node.leftChild != null) {
            getIn(node.leftChild, inOrderResult);
        }

        inOrderResult.add(node.message);

        if (node.rightChild != null) {
            getIn(node.rightChild, inOrderResult);
        }
    }

    @Override
    public int size() {
        return size;
    }
}
