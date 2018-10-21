package cz.zcu.students.kiwi.anlp.model;

import static org.junit.Assert.*;

public class UniformLanguageModelTest {

    public Vocabulary getVocabulary(int wordCount) {
        Vocabulary v = new Vocabulary();

        for (int i = 1; i <= wordCount; i++) {
            v.put("W" + i);
        }

        return v;
    }

    @org.junit.Test
    public void getProbabilityNoPadding() {
        UniformLanguageModel model = new UniformLanguageModel(getVocabulary(20));

        assertEquals(0.05, model.getProbability("W1"), 0.001);
        assertEquals(0.05, model.getProbability("nada"), 0.001);
    }

    @org.junit.Test
    public void getProbabilityPadding() {
        UniformLanguageModel model = new UniformLanguageModel(getVocabulary(20), 2);

        assertEquals(0.025, model.getProbability("W1"), 0.001);
        assertEquals(0.025, model.getProbability("nada"), 0.001);
    }


}
