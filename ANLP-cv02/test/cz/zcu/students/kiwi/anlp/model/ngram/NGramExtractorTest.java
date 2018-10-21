package cz.zcu.students.kiwi.anlp.model.ngram;

import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class NGramExtractorTest {

    @Test
    public void extract() {
        NGramExtractor extractor = new NGramExtractor();
        Collection<NGram> nGrams = extractor.extract(new String[]{"a", "b", "c"}, 1);

        assertEquals(3, nGrams.size());

        nGrams = extractor.extract(new String[]{"a", "b", "c"}, 2);

        assertEquals(2, nGrams.size());
    }

    @Test
    public void extractTrigramsPrefix() {
        NGramExtractor extractor = new NGramExtractor("_", null);

        Collection<NGram> nGrams = extractor.extract("a b c".split(" "), 3);

        NGram[] expected = new NGram[]{
                new NGram("_", "_", "a"),
                new NGram("_", "a", "b"),
                new NGram("a", "b", "c"),
        };

        assertArrayEquals(expected, nGrams.toArray());
    }

    @Test
    public void extractTrigramsSuffix() {
        NGramExtractor extractor = new NGramExtractor(null, "#");

        Collection<NGram> nGrams = extractor.extract("a b c".split(" "), 3);

        NGram[] expected = new NGram[]{
                new NGram("a", "b", "c"),
                new NGram("b", "c", "#"),
                new NGram("c", "#", "#"),
        };

        assertArrayEquals(expected, nGrams.toArray());
    }

    @Test
    public void extractQuadGramsPrefixSuffix() {
        NGramExtractor extractor = new NGramExtractor("^", "$");

        Collection<NGram> nGrams = extractor.extract("a b c d".split(" "), 4);

        NGram[] expected = new NGram[]{
                new NGram("^", "^", "^", "a"),
                new NGram("^", "^", "a", "b"),
                new NGram("^", "a", "b", "c"),
                new NGram("a", "b", "c", "d"),
                new NGram("b", "c", "d", "$"),
                new NGram("c", "d", "$", "$"),
                new NGram("d", "$", "$", "$"),
        };

        assertArrayEquals(expected, nGrams.toArray());
    }

}
