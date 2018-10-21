package cz.zcu.students.kiwi.anlp.a02;

import cz.zcu.students.kiwi.anlp.IAssignment;
import cz.zcu.students.kiwi.anlp.fileIO.BasicWordSetProvider;
import cz.zcu.students.kiwi.anlp.model.LanguageModel;
import cz.zcu.students.kiwi.anlp.model.NGramLanguageModel;
import cz.zcu.students.kiwi.anlp.model.UniformLanguageModel;
import cz.zcu.students.kiwi.anlp.model.Vocabulary;
import cz.zcu.students.kiwi.anlp.model.ngram.NGram;
import cz.zcu.students.kiwi.anlp.model.ngram.NGramExtractor;
import cz.zcu.students.kiwi.anlp.model.smoothing.ExpectationMaximizer;
import cz.zcu.students.kiwi.anlp.model.smoothing.LinearInterpolation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public final class Main_02 implements IAssignment {

    private static final int MAX_STEPS = 30;

    private int maxNGramOrder = 2;

    private boolean padNGrams = true;
    private String trainFile = "data/train.txt",
            heldOutFile = "data/heldout.txt",
            testFile = "data/test.txt";


    @Override
    public void run(PrintStream out, InputStream inputStream) throws IOException {
        NGramExtractor extractor = padNGrams ? new NGramExtractor(Vocabulary.SENTENCE, Vocabulary.SENTENCE_END) : new NGramExtractor();
        out.println("Using nGram extractor with" + (this.padNGrams ? "" : "out") + " padding nGrams");
        out.println();


        out.println("=== Loading language models ===");
        out.print("  opening '" + trainFile + "' ...");

        LanguageModel[] models = loadNGramLanguageModels(extractor, trainFile, maxNGramOrder);
        if (models == null) {
            System.err.println("Failed to load language model");
            System.exit(1);
        }
        out.println("loaded\n");

        out.println("=== Creating interpolation ===");
        LinearInterpolation interpolation = new LinearInterpolation(models);
        out.println("  loading held-out data from '" + this.heldOutFile + "'");

        ExpectationMaximizer adjuster = new ExpectationMaximizer(interpolation);
        int stepsTaken = adjuster.trainLambdaValues(loadNGrams(this.heldOutFile, extractor, this.maxNGramOrder), 0.01, MAX_STEPS);

        out.println("  lambdas maximized in " + stepsTaken + " steps");
        out.println("    lambda values = " + interpolation.getLambdasString());
        out.println();

        out.println("=== Testing language model ===");
        LanguageModelEvaluator evaluator = new LanguageModelEvaluator(interpolation, extractor, this.maxNGramOrder);
        Collection<String[]> testData = BasicWordSetProvider.stream(this.testFile, StandardCharsets.UTF_8)
                .collect(Collectors.toList());

        LanguageModelEvaluator.Stats stats = evaluator.evaluate(testData);
        stats.print(out, interpolation.getModelName());

        for (LanguageModel model : models) {
            evaluator = new LanguageModelEvaluator(model, extractor, this.maxNGramOrder);
            stats = evaluator.evaluate(testData);
            stats.print(out, model.getModelName());
        }

//        boolean result = evaluator.testBiGramLanguageModel(out, loadVocabulary(this.testFile));
//        out.println("Language model is " + (!result ? "not " : "") + "P-compliant");
    }

    private LanguageModel[] loadNGramLanguageModels(NGramExtractor extractor, String trainFile, int maxOrder) {
        NGramLanguageModel[] nGramModels = new NGramLanguageModel[maxOrder];
        for (int i = 1; i <= maxOrder; i++) {
            nGramModels[i - 1] = new NGramLanguageModel(i, extractor);
        }

        try (BasicWordSetProvider provider = new BasicWordSetProvider(trainFile, StandardCharsets.UTF_8)) {
            String[] sentence;
            while ((sentence = provider.next()) != null) {
                for (int i = 1; i <= maxOrder; i++) {
                    nGramModels[i - 1].addSentence(sentence);
                }
            }

            LanguageModel[] languageModels = new LanguageModel[maxOrder + 1];
            languageModels[0] = new UniformLanguageModel(nGramModels[1].getVocabulary());
            System.arraycopy(nGramModels, 0, languageModels, 1, nGramModels.length);

            return languageModels;
        } catch (IOException ex) {
            System.err.println("Whoop whoop, this is the sound of exception police! " + ex.getMessage());
            return null;
        }
    }

    private List<NGram> loadNGrams(String dataFile, NGramExtractor extractor, int order) throws IOException {
        List<NGram> nGrams = new ArrayList<>();
        BasicWordSetProvider.stream(dataFile, StandardCharsets.UTF_8)
                .forEach((sentence) -> nGrams.addAll(extractor.extract(sentence, order)));

        return nGrams;
    }

    private Vocabulary loadVocabulary(String dataFile) throws IOException {
        Vocabulary vocabulary = new Vocabulary();
        BasicWordSetProvider.stream(dataFile, StandardCharsets.UTF_8)
                .forEach((sentence) -> {
                    for (String s : sentence) {
                        vocabulary.put(s);
                    }
                });

        return vocabulary;
    }

    public static void main(String[] args) {
        Main_02 main = new Main_02();

        try {
            main.run(System.out, System.in);
        } catch (Exception e) {
            System.err.println("Error occured during runtime of " + main.getClass().getName());
            e.printStackTrace();
        }
    }
}
