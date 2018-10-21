package cz.zcu.students.kiwi.anlp.model.ngram;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class NGramOccurrenceTreeTest {

    private static NGramExtractor extractor = new NGramExtractor();
    private static String TEST_TEXT = "A B C A A B";

    private Collection<NGram> getNGRams(int order) {
        return extractor.extract(TEST_TEXT.split(" "), order);
    }

    @Test
    public void testFlatPutGet() {
        NGramOccurrenceTree tree = new NGramOccurrenceTree(1);

        for (NGram nGram : getNGRams(1)) {
            tree.put(nGram);
        }

        assertEquals(6, tree.getSize());
        assertEquals(3, tree.getOccurrence("A"));
        assertEquals(2, tree.getOccurrence("B"));
        assertEquals(1, tree.getOccurrence("C"));
    }

    @Test
    public void testDepthTwo() {
        NGramOccurrenceTree tree = new NGramOccurrenceTree(2);

        for (NGram nGram : getNGRams(2)) {
            tree.put(nGram);
        }

        assertEquals(5, tree.getSize());
        assertEquals(3, tree.getOccurrence("A"));
        assertEquals(1, tree.getOccurrence("B"));
        assertEquals(1, tree.getOccurrence("C"));

        assertEquals(2, tree.getOccurrence("A", "B"));
        assertEquals(2, tree.getOccurrence("A", "B"));
    }
}
