package cz.zcu.students.kiwi.anlp.model.ngram;

import java.util.HashMap;

public class NGramOccurrenceTree {

    private static final HashMap<NGramOccurrenceTree, Integer> ids = new HashMap<>();

    private final int maxOrder;
    private final HashMap<String, NGramOccurrenceTree> tree;

    private int totalEntries;

    public NGramOccurrenceTree(int maxOrder) {
        this.maxOrder = maxOrder;
        this.tree = new HashMap<>();
        this.totalEntries = 0;

        ids.put(this, ids.size());
    }

    public void put(NGram nGram) {
        String[] words = nGram.getWords();

        this.putRange(words);
    }

    private void putRange(String[] words) {
        this.totalEntries++;

        if (this.maxOrder < 1) {
            return;
        }

        String word = words[words.length - this.maxOrder];
        if (!this.tree.containsKey(word)) {
            this.tree.put(word, new NGramOccurrenceTree(maxOrder - 1));
        }

        this.tree.get(word).putRange(words);
    }

    public NGramOccurrenceTree getSubTree(String... words) {
        return this.getSubTree(words, 0, words.length);
    }

    public NGramOccurrenceTree getSubTree(String[] words, int from, int to) {
        NGramOccurrenceTree result = this;

        from = Math.max(from, 0);
        to = Math.min(to, words.length);

        for (int i = from; i < to; i++) {
            result = result.getByWord(words[i]);
            if (result == null) {
                return null;
            }
        }

        return result;
    }

    public int getOccurrence(String... words) {
        NGramOccurrenceTree node = this.getSubTree(words);
        if (node == null) {
            return 0;
        }
        return node.getSize();
    }

    public int getSize() {
        return this.totalEntries;
    }

    public boolean containsKey(String key) {
        return this.tree.containsKey(key);
    }

    protected NGramOccurrenceTree getByWord(String key) {
        return this.tree.getOrDefault(key, null);
    }

    @Override
    public String toString() {
        return "NGramOccurrenceTree{totalEntries=" + totalEntries + '}';
    }
}
