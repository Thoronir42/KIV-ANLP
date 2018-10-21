package cz.zcu.students.kiwi.anlp.model.smoothing;

import cz.zcu.students.kiwi.anlp.model.LanguageModel;
import cz.zcu.students.kiwi.anlp.model.Vocabulary;
import cz.zcu.students.kiwi.anlp.model.ngram.NGram;

import java.util.Arrays;
import java.util.function.Function;

public class LinearInterpolation implements LanguageModel {

    final LanguageModel[] models;
    final double[] lambdas;

    public LinearInterpolation(LanguageModel... models) {
        if (models.length < 1) {
            throw new IllegalArgumentException("There must be at least 1 model to perform linear interpolation between");
        }

        this.models = models;
        this.lambdas = new double[models.length];

        for (int i = 0; i < models.length; i++) {
            this.lambdas[i] = 1.0 / models.length;
        }
    }


    @Override
    public double getProbability(String word, String... history) {
        return sumProbInModels((model) -> model.getProbability(word, history));
    }

    @Override
    public double getProbability(NGram nGram) {
        return sumProbInModels((model) -> model.getProbability(nGram));
    }

    private double sumProbInModels(Function<LanguageModel, Double> callback) {
        double probSum = 0;
        double lambdaCompensation = 1;

        for (int k = 0; k < this.models.length; k++) {
            double kProb = callback.apply(models[k]);
            if (kProb == 0) {
                lambdaCompensation += this.lambdas[k];
            } else {
                probSum += this.lambdas[k] * kProb;
            }
        }

        return probSum * lambdaCompensation;
    }

    @Override
    public Vocabulary getVocabulary() {
        return this.models[0].getVocabulary();
    }

    @Override
    public String getModelName() {
        return "Linear interpolation of " + this.models.length + " models";
    }

    public String getLambdasString() {
        return Arrays.toString(this.lambdas);
    }
}
