package implementations;

import interfaces.AbstractTree;

import java.util.*;

//Recursive abstract data structure
public class Tree<E> implements AbstractTree<E> {

    private E value;

    private Tree<E> parent;

    private List<Tree<E>> children;

    public Tree(E value) {
        this.value = value;
        parent = null;
        children = new ArrayList<>();
    }

    //Varargs
    @SafeVarargs
    public Tree(E value, Tree<E>... subtrees) {
        this(value);
        for (Tree<E> subtree : subtrees) {
            children.add(subtree);
            subtree.parent = this;
        }
    }

    @Override
    public List<E> orderBfs() {
        List<E> result = new ArrayList<>();
        if (value == null) {
            return result;
        }
        Deque<Tree<E>> childrenQueue = new ArrayDeque<>();

        childrenQueue.offer(this);

        while (!childrenQueue.isEmpty()) {
            Tree<E> current = childrenQueue.poll();

            result.add(current.value);

            for (Tree<E> child : current.children) {
                childrenQueue.offer(child);
            }
        }
        return result;
    }

    @Override
    public List<E> orderDfs() {
        if (value == null) {
            return null;
        }
        List<E> result = new ArrayList<>();
        doDfs(this, result);
        return result;
    }

    private void doDfs(Tree<E> node, List<E> result) {
        for (Tree<E> child : node.children) {
            doDfs(child, result);
        }
        result.add(node.value);
    }

    @Override
    public void addChild(E parentKey, Tree<E> child) {
        Tree<E> parentTree = findBfs(parentKey);
        if (parentTree == null) {
            throw new IllegalArgumentException();
        }
        parentTree.children.add(child);
        child.parent = parentTree;
    }

    private Tree<E> findBfs(E parentKey) {
        if (value == null) {
            return null;
        }
        Deque<Tree<E>> childrenQueue = new ArrayDeque<>();
        childrenQueue.offer(this);

        while (!childrenQueue.isEmpty()) {
            Tree<E> current = childrenQueue.poll();

            if (current.value.equals(parentKey)) {
                return current;
            }

            for (Tree<E> child : current.children) {
                childrenQueue.offer(child);
            }
        }

        return null;
    }

    @Override
    public void removeNode(E nodeKey) {
        Tree<E> toRemove = findBfs(nodeKey);
        if (toRemove == null) {
            throw new IllegalArgumentException();
        }
        for (Tree<E> child : toRemove.children) {
            child.parent = null;
        }
        toRemove.children.clear();
        Tree<E> parentToRemove = toRemove.parent;
        if (parentToRemove != null) {
            parentToRemove.children.remove(toRemove);
        }
        toRemove.value = null;
    }

    @Override
    public void swap(E firstKey, E secondKey) {
        Tree<E> firstTree = findBfs(firstKey);
        Tree<E> secondTree = findBfs(secondKey);

        if (firstTree == null || secondTree == null) {
            throw new IllegalArgumentException();
        }

        Tree<E> firstParent = firstTree.parent;
        Tree<E> secondParent = secondTree.parent;

        if (firstParent == null) {
            swapRoot(secondTree);
            return;
        } else if (secondParent == null){
            swapRoot(firstTree);
            return;
        }

        firstTree.parent = secondParent;
        secondTree.parent = firstParent;

        int firstIndex = firstParent.children.indexOf(firstTree);
        int secondIndex = secondParent.children.indexOf(secondTree);

        firstParent.children.set(firstIndex, secondTree);
        secondParent.children.set(secondIndex, firstTree);
    }

    private void swapRoot(Tree<E> tree) {
        value = tree.value;
        parent = null;
        children = tree.children;
        tree.parent = null;
    }
}
