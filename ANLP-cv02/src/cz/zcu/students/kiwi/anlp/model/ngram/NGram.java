package cz.zcu.students.kiwi.anlp.model.ngram;

import java.util.*;

public class NGram {

    private final String[] words;

    public NGram(int order) {
        this.words = new String[order];
    }

    public NGram(String... words) {
        this.words = words;
    }

    public NGram(Collection<String> list) {
        this(list.toArray(new String[0]));
    }

    protected void setWord(int i, String word) {
        this.words[i] = word;
    }

    public String[] getWords() {
        return words;
    }

    public String getWord(int n) {
        return this.words[n];
    }

    public int getOrder() {
        return this.words.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NGram nGram = (NGram) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(words, nGram.words);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(words);
    }

    @Override
    public String toString() {
        return NGram.class.getName() + Arrays.toString(this.words);
    }

    public NGram slice(int from, int to) {
        return new NGram(this.wSlice(from, to));
    }

    public String[] wSlice(int from, int to) {
        return Arrays.copyOfRange(this.words, from, to);
    }
}
