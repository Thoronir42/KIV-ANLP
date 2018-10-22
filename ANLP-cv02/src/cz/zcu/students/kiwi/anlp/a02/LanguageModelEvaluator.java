package cz.zcu.students.kiwi.anlp.a02;

import cz.zcu.students.kiwi.anlp.model.LanguageModel;
import cz.zcu.students.kiwi.anlp.model.Vocabulary;
import cz.zcu.students.kiwi.anlp.model.ngram.NGram;
import cz.zcu.students.kiwi.anlp.model.ngram.NGramExtractor;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LanguageModelEvaluator {

    private final LanguageModel model;
    private final NGramExtractor extractor;
    private final int maxOrder;

    public LanguageModelEvaluator(LanguageModel model, NGramExtractor extractor, int maxOrder) {
        this.model = model;
        this.extractor = extractor;
        this.maxOrder = maxOrder;
    }

    public Stats evaluate(Collection<String[]> testData) {
        double k = 0;

        double probSum = 0, log2 = Math.log(2);
        for (String[] sentence : testData) {
            for (NGram nGram : extractor.extract(sentence, maxOrder)) {
                double prob = this.model.getProbability(nGram);
                probSum += (Math.log(prob) / log2);
                k++;
            }
        }

        double entropy = - probSum / k;

        return new Stats(entropy, Math.pow(2, entropy));
    }

    // originally in Test.java
    public boolean testBiGramLanguageModel(PrintStream out, Vocabulary vocabulary) {
        List<String> words = vocabulary.getWords();
        HashMap<NGram, Double> probabilitySumsByWord = new HashMap<>();

        out.println("Starting parallel probability sum");
        words.parallelStream()
                .map(NGram::new)
                .forEach((history) -> {
                    double sum = words.parallelStream()
                            .mapToDouble((word) -> this.model.getProbability(word, history.getWords()))
                            .sum();

                    synchronized (probabilitySumsByWord) {
                        probabilitySumsByWord.put(history, sum);
                    }
                });

        out.println("Validating results");

        AtomicBoolean result = new AtomicBoolean(true);

        probabilitySumsByWord.forEach((history, sum) -> {
            if (sum < 0.99d || sum > 1.01d) {
                result.set(false);
                out.println("  Word=" + Arrays.toString(history.getWords()) + ", Prob=" + sum);
            }
        });

        return result.get();
    }

    public static class Stats {
        private final double entropy;
        private final double perplexity;

        Stats(double entropy, double perplexity) {
            this.entropy = entropy;
            this.perplexity = perplexity;
        }

        public double getEntropy() {
            return entropy;
        }

        public double getPerplexity() {
            return perplexity;
        }

        public void print(PrintStream out, String name) {
            out.println(name);
            out.println("  entropy    : " + this.getEntropy());
            out.println("  perplexity : " + this.getPerplexity());
        }
    }
}
