package cz.zcu.students.kiwi.anlp.model;

import cz.zcu.students.kiwi.anlp.model.ngram.NGram;

public interface LanguageModel {

    double getProbability(String word, String ...history);

    double getProbability(NGram nGram);

    Vocabulary getVocabulary();

    String getModelName();

}
