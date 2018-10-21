package cz.zcu.students.kiwi.anlp.model;

import cz.zcu.students.kiwi.anlp.model.ngram.NGram;
import cz.zcu.students.kiwi.anlp.model.ngram.NGramExtractor;
import org.junit.Test;

import static org.junit.Assert.*;

public class NGramLanguageModelTest {

    @Test
    public void testUnigramEncounteredWord() {
        NGramLanguageModel model = new NGramLanguageModel(1, new NGramExtractor());
        model.addSentence("ahoj ahoj ahoj banan".split(" "));

        assertEquals(0.75, model.getProbability("ahoj"), 0.001);
        assertEquals(0.25, model.getProbability("banan"), 0.001);
    }

    @Test
    public void testBigramEncounteredWord() {
        NGramLanguageModel model = new NGramLanguageModel(2, new NGramExtractor());
        model.addSentence("be be fe".split(" "));
        model.addSentence("be be fe le me".split(" "));
        model.addSentence("te be fe le me".split(" "));

        double probability = model.getProbability("be", "be");
        assertEquals(0.4, probability, 0.001);
    }

    @Test
    public void testBigramEncounteredWordNgram() {
        NGramLanguageModel model = new NGramLanguageModel(2, new NGramExtractor());
        model.addSentence("be be fe".split(" "));
        model.addSentence("be be fe le me".split(" "));
        model.addSentence("te be fe le me".split(" "));

        assertEquals(0.4, model.getProbability(new NGram("be", "be")), 0.001);
        assertEquals(0.4, model.getProbability(new NGram("foo", "be", "be")), 0.001);
    }

    @Test
    public void testTrigramEncounteredWord() {
        NGramLanguageModel model = new NGramLanguageModel(3, new NGramExtractor());
        model.addSentence("be be fe".split(" "));
        model.addSentence("be be le fe le me".split(" "));
        model.addSentence("te be fe le me".split(" "));

        double probability = model.getProbability("fe", "be", "be");
        assertEquals(0.5, probability, 0.001);
    }
}
