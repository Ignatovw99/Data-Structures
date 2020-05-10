import java.util.*;
import java.util.stream.Collectors;

class Wordcruncher {

    private final String targetString;

    private final List<String> inputStrings;

    private Map<Integer, TreeSet<String>> tree;

    private Map<String, Integer> wordsByCount; // How many times can you use a word

    private List<String> buffer;

    private List<String> out;

    public Wordcruncher(String targetString, List<String> inputStrings) {
        this.targetString = targetString;
        this.inputStrings = inputStrings;
        tree = new HashMap<>();
        wordsByCount = new HashMap<>();
        buffer = new ArrayList<>();
        out = new ArrayList<>();
    }

    public List<String> formRequiredString() {
        List<String> extractedStrings = extractOnlyStringsContainingInTheTargetString();

        for (String word : extractedStrings) {
            wordsByCount.putIfAbsent(word, 0);
            wordsByCount.put(word, wordsByCount.get(word) + 1);

            int index = targetString.indexOf(word);

            while (index != -1) {
                if (!tree.containsKey(index)) {
                    tree.put(index, new TreeSet<>());
                }
                tree.get(index).add(word);
                index = targetString.indexOf(word, index + 1);
            }
        }

        dfsTraversal(0);

        return out;
    }

    private void dfsTraversal(int stringIndex) {
        if (stringIndex >= targetString.length()) {
            getResult();
        } else {
            if (!tree.containsKey(stringIndex)) {
                return;
            }
            for (String string : tree.get(stringIndex)) {
                if (wordsByCount.get(string) > 0) {
                    buffer.add(string);
                    wordsByCount.put(string, wordsByCount.get(string) - 1);
                    dfsTraversal(stringIndex + string.length());
                    wordsByCount.put(string, wordsByCount.get(string) + 1);
                    buffer.remove(buffer.size() - 1);
                }
            }
        }
    }

    private void getResult() {
        String result = String.join("", buffer);
        if (result.equals(targetString)) {
            out.add(String.join(" ", buffer) + System.lineSeparator());
        }
    }

    private List<String> extractOnlyStringsContainingInTheTargetString() {
        return inputStrings.stream()
                .filter(targetString::contains)
                .collect(Collectors.toList());
    }
}
