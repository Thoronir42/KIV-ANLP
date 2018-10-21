package cz.zcu.students.kiwi.anlp.model.ngram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NGramExtractor {

    private String prefix;
    private String suffix;

    public NGramExtractor() {

    }

    public NGramExtractor(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public List<String[]> wExtract(String[] sentence, int order) {
        List<String[]> nGrams = new ArrayList<>();

        if (sentence.length < order) {
            return nGrams;
        }

        if (this.prefix != null) {
            this.extractPrefixesTo(nGrams, sentence, order);
        }

        int ngramCount = sentence.length - order + 1;
        for (int i = 0; i < ngramCount; i++) {
            nGrams.add(Arrays.copyOfRange(sentence, i, i + order));
        }

        if (this.suffix != null) {
            this.extractSuffixesTo(nGrams, sentence, order);
        }

        return nGrams;
    }

    public List<NGram> extract(String[] sentence, int order) {
        return wExtract(sentence, order).stream()
                .map(NGram::new)
                .collect(Collectors.toList());
    }

    private void extractPrefixesTo(List<String[]> nGrams, String[] sentence, int order) {
        String[] padding = new String[order];
        for (int i = 0; i < padding.length; i++) {
            padding[i] = this.prefix;
        }

        for (int w = order - 1; w >= 1; w--) {
            String[] words = Arrays.copyOf(padding, padding.length);
            // copy w words from sentence start
            if (order - w >= 0) System.arraycopy(sentence, 0, words, w, order - w);
            nGrams.add(words);
        }
    }

    private void extractSuffixesTo(List<String[]> nGrams, String[] sentence, int order) {
        String[] padding = new String[order];
        for (int i = 0; i < padding.length; i++) {
            padding[i] = this.suffix;
        }

        for (int w = order - 1; w >= 1; w--) {
            String[] words = Arrays.copyOf(padding, padding.length);

            System.arraycopy(sentence, sentence.length - w, words, 0, w);
            nGrams.add(words);
        }
    }
}
