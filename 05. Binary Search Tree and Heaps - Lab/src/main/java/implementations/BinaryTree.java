package implementations;

import interfaces.AbstractBinaryTree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BinaryTree<E> implements AbstractBinaryTree<E> {

    private E key;

    private BinaryTree<E> leftChild;

    private BinaryTree<E> rightChild;

    public BinaryTree(E key, BinaryTree<E> leftChild, BinaryTree<E> rightChild) {
        setKey(key);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public E getKey() {
        return key;
    }

    @Override
    public AbstractBinaryTree<E> getLeft() {
        return leftChild;
    }

    @Override
    public AbstractBinaryTree<E> getRight() {
        return rightChild;
    }

    @Override
    public void setKey(E key) {
        this.key = key;
    }

    private String getPadding(int indent) {
        StringBuilder paddingBuilder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            paddingBuilder.append(" ");
        }
        return paddingBuilder.toString();
    }

    @Override
    public String asIndentedPreOrder(int indent) {
        StringBuilder result = new StringBuilder();

        result.append(getPadding(indent))
                .append(getKey());

        if (getLeft() != null) {
            String leftResult = getLeft().asIndentedPreOrder(indent + 2);
            result.append(System.lineSeparator())
                    .append(leftResult);
        }

        if (getRight() != null) {
            String rightResult = getRight().asIndentedPreOrder(indent + 2);
            result.append(System.lineSeparator())
                    .append(rightResult);
        }

        return result.toString();
    }

    @Override
    public List<AbstractBinaryTree<E>> preOrder() {
        List<AbstractBinaryTree<E>> preOrderResult = new ArrayList<>();

        preOrderResult.add(this);

        if (getLeft() != null) {
            preOrderResult.addAll(getLeft().preOrder());
        }

        if (getRight() != null) {
            preOrderResult.addAll(getRight().preOrder());
        }

        return preOrderResult;
    }

    @Override
    public List<AbstractBinaryTree<E>> inOrder() {
        List<AbstractBinaryTree<E>> inOrderResult = new ArrayList<>();

        if (getLeft() != null) {
            inOrderResult.addAll(getLeft().inOrder());
        }

        inOrderResult.add(this);

        if (getRight() != null) {
            inOrderResult.addAll(getRight().inOrder());
        }

        return inOrderResult;
    }

    @Override
    public List<AbstractBinaryTree<E>> postOrder() {
        List<AbstractBinaryTree<E>> postOrderResult = new ArrayList<>();

        if (getLeft() != null) {
            postOrderResult.addAll(getLeft().postOrder());
        }

        if (getRight() != null) {
            postOrderResult.addAll(getRight().postOrder());
        }

        postOrderResult.add(this);

        return postOrderResult;
    }

    @Override
    public void forEachInOrder(Consumer<E> consumer) {
        if (getLeft() != null) {
            getLeft().forEachInOrder(consumer);
        }

        consumer.accept(getKey());

        if (getRight() != null) {
            getRight().forEachInOrder(consumer);
        }
    }
}
