package implementations;

import interfaces.AbstractTree;

import java.util.*;
import java.util.stream.Collectors;

public class Tree<E> implements AbstractTree<E> {

    private E key;

    private Tree<E> parent;

    private List<Tree<E>> children;

    public Tree(E key) {
        this.key = key;
        parent = null;
        children = new ArrayList<>();
    }

    public Tree(E key, Tree<E>... children) {
        this(key);
        this.children.addAll(Arrays.asList(children));
        for (Tree<E> child : children) {
            child.setParent(this);
        }
    }

    private String getPadding(int size) {
        StringBuilder paddingBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            paddingBuilder.append(" ");
        }
        return paddingBuilder.toString();
    }

    //BFS traversal
    private List<Tree<E>> traverseTreeInBreadth(Tree<E> rootTree) {
        List<Tree<E>> trees = new ArrayList<>();

        Deque<Tree<E>> queue = new ArrayDeque<>();

        queue.offer(rootTree);

        while (!queue.isEmpty()) {
            Tree<E> currentTree = queue.poll();

            trees.add(currentTree);

            for (Tree<E> child : currentTree.getChildren()) {
                queue.offer(child);
            }
        }

        return trees;
    }

    //DFS traversal
    private void traverseTreeInDepth(Tree<E> tree, List<Tree<E>> trees) {
        trees.add(tree);

        for (Tree<E> child : tree.getChildren()) {
            traverseTreeInDepth(child, trees);
        }
    }

    private void traverseTreeWithRecurrence(Tree<E> currentTree, int indent, StringBuilder builder) {
        builder
                .append(getPadding(indent))
                .append(currentTree.getKey())
                .append(System.lineSeparator());

        for (Tree<E> child : currentTree.getChildren()) {
            traverseTreeWithRecurrence(child, indent + 2, builder);
        }
    }

    private boolean isLeaf() {
        return getParent() != null && getChildren().isEmpty();
    }

    private boolean isMiddle() {
        return getParent() != null && getChildren().size() > 0;
    }

    @Override
    public void setParent(Tree<E> parent) {
        this.parent = parent;
    }

    @Override
    public void addChild(Tree<E> child) {
        children.add(child);
    }

    @Override
    public Tree<E> getParent() {
        return parent;
    }

    @Override
    public List<Tree<E>> getChildren() {
        return children;
    }

    @Override
    public E getKey() {
        return key;
    }

    @Override
    public String getAsString() {
        StringBuilder builder = new StringBuilder();

        traverseTreeWithRecurrence(this, 0, builder);

        return builder.toString().trim();
    }

    @Override
    public List<Tree<E>> getLeafs() {
        return traverseTreeInBreadth(this)
                .stream()
                .filter(Tree::isLeaf)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> getLeafKeys() {
        return getLeafs()
                .stream()
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<E> getMiddleKeys() {
        return traverseTreeInBreadth(this)
                .stream()
                .filter(Tree::isMiddle)
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    private void findDeepestLeftmostNodeWithTopDownApproach(List<Tree<E>> deepestLeftmostNode, Tree<E> currentTree, int[] deepestPath, int currentPath) {
        if (currentPath > deepestPath[0]) {
            deepestLeftmostNode.set(0, currentTree);
            deepestPath[0] = currentPath;
        }

        for (Tree<E> child : currentTree.getChildren()) {
            findDeepestLeftmostNodeWithTopDownApproach(deepestLeftmostNode, child, deepestPath, currentPath + 1);
        }
    }

    private Tree<E> getDeepestLeftmostNodeWithBottomUpApproach() {
        //BFS is used first and then bottom up approach -> this algorithm is not so optimized
        List<Tree<E>> leafs = getLeafs();

        Tree<E> deepestLeftmostNode = null;
        int deepestNodePath = 0;

        for (Tree<E> leaf : leafs) {
            int currentNodePath = getPathToRoot(leaf);
            if (currentNodePath > deepestNodePath) {
                deepestNodePath = currentNodePath;
                deepestLeftmostNode = leaf;
            }
        }

        return deepestLeftmostNode;
    }

    private int getPathToRoot(Tree<E> tree) {
        int steps = 0;

        Tree<E> current = tree;

        while (current.getParent() != null) {
            steps++;
            current = current.getParent();
        }

        return steps;
    }

    @Override
    public Tree<E> getDeepestLeftmostNode() {
        Tree<E> deepestLeftmostNode = getDeepestLeftmostNodeWithBottomUpApproach();

//        The following top down approach solution is more optimized, because it traverses the tree only once
//        |
//        V

//        List<Tree<E>> deepestLeftmostNode = new ArrayList<>(Collections.singletonList(this));
//        findDeepestLeftmostNodeWithTopDownApproach(deepestLeftmostNode, this, new int[] { 0 }, 0);

        return deepestLeftmostNode;
    }

    private List<Tree<E>> getLeftmostLongestPathNodes(Tree<E> tree) {
        Tree<E> deepestLeftmostNode = tree.getDeepestLeftmostNode();
        List<Tree<E>> leftmostLongestPath = new ArrayList<>();

        Tree<E> currentNode = deepestLeftmostNode;

        while (currentNode != null) {
            leftmostLongestPath.add(currentNode);
            currentNode = currentNode.getParent();
        }

        Collections.reverse(leftmostLongestPath);

        return leftmostLongestPath;
    }

    @Override
    public List<E> getLongestPath() {
        List<Tree<E>> leftmostLongestPath = getLeftmostLongestPathNodes(this);
        return leftmostLongestPath
                .stream()
                .map(Tree::getKey)
                .collect(Collectors.toList());
    }

    private void findTreePathsWithGivenSum(Tree<E> tree, int givenSum, int sum, List<List<E>> paths) {
        if (givenSum == sum) {
            List<E> keys = new ArrayList<>();
            Tree<E> current = tree;
            while (current != null) {
                keys.add(current.getKey());
                current = current.getParent();
            }
            Collections.reverse(keys);
            paths.add(keys);
            return;
        } else if (givenSum < sum) {
            return;
        }
        for (Tree<E> child : tree.getChildren()) {
            findTreePathsWithGivenSum(child, givenSum, sum + (int)child.getKey(), paths);
        }
    }

    @Override
    public List<List<E>> pathsWithGivenSum(int sum) {
        List<List<E>> paths = new ArrayList<>();
        findTreePathsWithGivenSum(this, sum, (int) getKey(), paths);
        return paths;
    }

    private void getSubtreeSum(int[] sum) {
        sum[0] += (int) getKey();

        for (Tree<E> child : children) {
            child.getSubtreeSum(sum);
        }
    }

    private void traverseSubtreeForGivenSum(Tree<E> tree, int sum, List<Tree<E>> subtrees) {
        int[] subtreeSum = new int[] {0};
        tree.getSubtreeSum(subtreeSum);

        if (subtreeSum[0] == sum) {
            subtrees.add(tree);
        }

        for (Tree<E> child : tree.getChildren()) {
            traverseSubtreeForGivenSum(child, sum, subtrees);
        }
    }

    @Override
    public List<Tree<E>> subTreesWithGivenSum(int sum) {
        List<Tree<E>> subtrees = new ArrayList<>();

        traverseSubtreeForGivenSum(this, sum, subtrees);

        return subtrees;
    }
}
