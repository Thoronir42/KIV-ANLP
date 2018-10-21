package cz.zcu.students.kiwi.anlp.model;

import cz.zcu.students.kiwi.anlp.model.ngram.NGram;

public class UniformLanguageModel implements LanguageModel {

    private final Vocabulary vocabulary;
    private final double vocabularySizePadding;

    public UniformLanguageModel(Vocabulary vocabulary) {
        this(vocabulary, 1);
    }

    public UniformLanguageModel(Vocabulary vocabulary, double vocabularySizePadding) {
        this.vocabulary = vocabulary;
        this.vocabularySizePadding = vocabularySizePadding;
    }

    public double getProbability() {
        double totalSize = this.vocabulary.size() * this.vocabularySizePadding;
        return 1.0 / totalSize;
    }

    @Override
    public double getProbability(String word, String... history) {
        return this.getProbability();
    }

    @Override
    public double getProbability(NGram nGram) {
        return this.getProbability();
    }

    @Override
    public Vocabulary getVocabulary() {
        return this.vocabulary;
    }

    public String getModelName() {
        return "Uniform(" + this.vocabulary.size() + " words)";
    }
}
