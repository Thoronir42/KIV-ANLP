package cz.zcu.students.kiwi.anlp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Vocabulary {
    public static final String SENTENCE = "<s>";
    public static final String SENTENCE_END = "</s>";

    private List<Entry> entries;
    private Map<String, Integer> wordToKeyMap;

    public Vocabulary(String... initialWords) {
        this.entries = new ArrayList<>();
        this.wordToKeyMap = new HashMap<>();

        for (String s : initialWords) {
            this.put(s);
        }
    }


    public void put(String word) {
        this.put(word, true);
    }

    public void put(String word, boolean increaseOccurrence) {
        int entryIndex = wordToKeyMap.getOrDefault(word, -1);
        if (entryIndex == -1) {
            entryIndex = entries.size();
            entries.add(new Entry(word));
            wordToKeyMap.put(word, entryIndex);
        }

        if (increaseOccurrence) {
            Entry entry = entries.get(entryIndex);
            entry.occurrence++;
        }
    }

    public String getWordByKey(int key) {
        if (key < 0 || key >= entries.size()) {
            throw new IllegalArgumentException("word key (" + key + ") must range between 0 and " + (entries.size() - 1));
        }

        return entries.get(key).word;
    }

    public String[] getWordByKey(int key[]) {
        String[] words = new String[key.length];
        for (int i = 0; i < words.length; i++) {
            words[i] = getWordByKey(key[i]);
        }

        return words;
    }

    public int getWordKey(String word) {
        Integer key = this.wordToKeyMap.get(word);
        return (key != null ? key : -1);
    }

    public int[] getWordKey(String[] words) {
        int[] keys = new int[words.length];
        for (int i = 0; i < words.length; i++) keys[i] = getWordKey(words[i]);
        return keys;
    }

    public int size() {
        return this.entries.size();
    }

    public List<String> getWords() {
        return this.entries.stream()
                .map((entry) -> entry.word)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getWeightedVocabulary() {
        return this.entries.stream()
                .collect(Collectors.toMap(entry -> entry.word, entry -> entry.occurrence));
    }

    static class Entry {
        private final String word;
        private int occurrence;

        Entry(String word) {
            this.word = word;
            this.occurrence = 1;
        }
    }


}
