package cz.zcu.students.kiwi.anlp.model;

import cz.zcu.students.kiwi.anlp.model.ngram.NGram;
import cz.zcu.students.kiwi.anlp.model.ngram.NGramExtractor;
import cz.zcu.students.kiwi.anlp.model.ngram.NGramOccurrenceTree;

import java.util.Collection;

public class NGramLanguageModel implements LanguageModel {

    private final int order;
    private final Vocabulary vocabulary;
    private final NGramExtractor ngramExtractor;

    protected final NGramOccurrenceTree nGramOccurrence;

    public NGramLanguageModel(int order, NGramExtractor nGramExtractor) {
        if (order <= 0) {
            throw new IllegalArgumentException("Language model order must not be greater than zero, " + order + " given.");
        }

        this.vocabulary = new Vocabulary();
        this.order = order;
        this.ngramExtractor = nGramExtractor;
        this.nGramOccurrence = new NGramOccurrenceTree(order);
    }

    public void addSentence(String[] words) {
        Collection<NGram> nGrams = this.ngramExtractor.extract(words, this.order);

        for (NGram nGram : nGrams) {
            this.nGramOccurrence.put(nGram);
        }

        for (String w : words) {
            this.vocabulary.put(w);
        }
    }

    @Override
    public double getProbability(String word, String... history) {
        if (history.length < this.order - 1) {
            throw new IllegalArgumentException("Word probability history length must be at least `order - 1` (" + (this.order - 1) + "), got: " + history.length);
        }

        NGramOccurrenceTree tree = this.nGramOccurrence.getSubTree(history);
        return getWordProbability(tree, word);
    }

    @Override
    public double getProbability(NGram nGram) {
        int nGramOrder = nGram.getOrder();
        if (nGramOrder < this.order) {
            throw new IllegalArgumentException("NGram order must be at least equal to " + (this.order - 1) + ", got: " + nGramOrder);
        }

        NGramOccurrenceTree tree = this.nGramOccurrence.getSubTree(nGram.getWords(), nGramOrder - this.order, nGramOrder - 1);
        return getWordProbability(tree, nGram.getWord(nGramOrder - 1));
    }

    private double getWordProbability(NGramOccurrenceTree tree, String word) {
        if (tree == null) {
            return 0;
        }

        // MLE:
        double wordOccurrence = tree.getOccurrence(word);
        double historyOccurrence = tree.getSize();
        if (historyOccurrence == 0) {
            throw new ArithmeticException("Division by 0");
        }
        return wordOccurrence / historyOccurrence;
    }

    @Override
    public Vocabulary getVocabulary() {
        return this.vocabulary;
    }

    @Override
    public String getModelName() {
        return this.order + "-gram language model";
    }
}
