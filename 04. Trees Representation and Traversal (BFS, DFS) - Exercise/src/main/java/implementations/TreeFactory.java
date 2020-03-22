package implementations;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TreeFactory {

    private Map<Integer, Tree<Integer>> nodesByKeys;

    public TreeFactory() {
        this.nodesByKeys = new LinkedHashMap<>();
    }

    public Tree<Integer> createTreeFromStrings(String[] input) {
        for (String params : input) {
            int[] parentChildPair = Arrays.stream(params.split("\\s+"))
                    .mapToInt(Integer::parseInt)
                    .toArray();

            int parentKey = parentChildPair[0];
            int childKey = parentChildPair[1];

            addEdge(parentKey, childKey);
        }
        return getRoot();
    }

    private Tree<Integer> getRoot() {
        for (Tree<Integer> subtree : nodesByKeys.values()) {
            if (subtree.getParent() == null) {
                return subtree;
            }
        }
        return null;
    }

    public Tree<Integer> createNodeByKey(int key) {
        nodesByKeys.putIfAbsent(key, new Tree<>(key));
        return nodesByKeys.get(key);
    }

    public void addEdge(int parent, int child) {
        Tree<Integer> parentByKey = createNodeByKey(parent);
        Tree<Integer> childByKey = createNodeByKey(child);

        parentByKey.addChild(childByKey);
        childByKey.setParent(parentByKey);
    }
}



