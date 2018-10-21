package cz.zcu.students.kiwi.anlp.model.smoothing;

import cz.zcu.students.kiwi.anlp.model.LanguageModel;
import cz.zcu.students.kiwi.anlp.model.Vocabulary;
import cz.zcu.students.kiwi.anlp.model.ngram.NGram;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinearInterpolationTest {

    private LanguageModel mockModel(double probability) {
        return new LanguageModel() {
            @Override
            public double getProbability(String word, String... history) {
                return probability;
            }

            @Override
            public double getProbability(NGram nGram) {
                return probability;
            }

            @Override
            public Vocabulary getVocabulary() {
                return null;
            }

            @Override
            public String getModelName() {
                return "Abstract language model";
            }
        };
    }

    @Test
    public void getProbability() {
        LinearInterpolation interpolation = new LinearInterpolation(
                mockModel(0.2),
                mockModel(0.6),
                mockModel(0.4),
                mockModel(0.8)
        );

        assertEquals(0.5, interpolation.getProbability(""), 0.01);

    }
}
