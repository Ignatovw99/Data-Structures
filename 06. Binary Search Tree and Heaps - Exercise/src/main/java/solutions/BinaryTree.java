package solutions;

import model.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BinaryTree {

    private int value;

    private BinaryTree left;

    private BinaryTree right;

    public BinaryTree(int key, BinaryTree first, BinaryTree second) {
        value = key;
        left = first;
        right = second;
    }

    public Integer findLowestCommonAncestor(int first, int second) {
//        return findLowestCommonAncestorRecursive(first, second);
        return findLowestCommonAncestorIterative(first, second);
    }

    private boolean isAncestor(BinaryTree binaryTree, int value) {
        if (binaryTree == null) {
            return false;
        }

        if (binaryTree.value == value) {
            return true;
        }

        return isAncestor(binaryTree.left, value) || isAncestor(binaryTree.right, value);
    }

    private void searchForLowestCommonAncestorRecursive(BinaryTree binaryTree, int first, int second, int[] lowestCommonAncestor) {
        if (binaryTree == null) {
            return;
        }

        if (isAncestor(binaryTree, first) && isAncestor(binaryTree, second)) {
            lowestCommonAncestor[0] = binaryTree.value;
        } else {
            return;
        }
        searchForLowestCommonAncestorRecursive(binaryTree.left, first, second, lowestCommonAncestor);
        searchForLowestCommonAncestorRecursive(binaryTree.right, first, second, lowestCommonAncestor);
    }

    private Integer findLowestCommonAncestorRecursive(int first, int second) {
        int[] lowestCommonAncestor = { 0 };
        searchForLowestCommonAncestorRecursive(this, first, second, lowestCommonAncestor);
        return lowestCommonAncestor[0];
    }

    private boolean findNodePath(BinaryTree binaryTree, int valueToBeFound, List<Integer> pathResult) {
        if (binaryTree == null) {
            return false;
        }
        if (binaryTree.value == valueToBeFound) {
            return true;
        }

        pathResult.add(binaryTree.value);

        boolean leftPathResult = findNodePath(binaryTree.left, valueToBeFound, pathResult);
        boolean rightPathResult = findNodePath(binaryTree.right, valueToBeFound, pathResult);

        if (leftPathResult || rightPathResult) {
            return true;
        } else {
            pathResult.remove(Integer.valueOf(binaryTree.value));
            return false;
        }
    }

    private List<Integer> findPath(int valueToBeFound) {
        List<Integer> pathResult = new ArrayList<>();
        findNodePath(this, valueToBeFound, pathResult);
        return pathResult;
    }

    private Integer findLowestCommonAncestorIterative(int first, int second) {
        List<Integer> firstPath = findPath(first);
        List<Integer> secondPath = findPath(second);

        int smallerPath = Math.min(firstPath.size(), secondPath.size());

        int i = 0;
        for (; i < smallerPath; i++) {
            if (!firstPath.get(i).equals(secondPath.get(i))) {
                break;
            }
        }

        return firstPath.get(i - 1);
    }

    private void traverseTree(BinaryTree binaryTree, int offset, int level, Map<Integer, Pair<Integer, Integer>> offsetToValueLevel) {
        if (binaryTree == null) {
            return;
        }

        Pair<Integer, Integer> currentValueLevel = offsetToValueLevel.get(offset);
        if (currentValueLevel == null || level < currentValueLevel.getValue()) {
            offsetToValueLevel.put(offset, new Pair<>(binaryTree.value, level));
        }

        traverseTree(binaryTree.left, offset - 1, level + 1, offsetToValueLevel);
        traverseTree(binaryTree.right, offset + 1, level + 1, offsetToValueLevel);
    }

    public List<Integer> topView() {
        Map<Integer, Pair<Integer, Integer>> offsetToValueLevel = new TreeMap<>();

        traverseTree(this, 0, 1, offsetToValueLevel);

        return offsetToValueLevel.values()
                .stream()
                .map(Pair::getKey)
                .collect(Collectors.toList());
    }
}
