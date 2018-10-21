package cz.zcu.students.kiwi.anlp.model.smoothing;

import cz.zcu.students.kiwi.anlp.model.ngram.NGram;

import java.util.Arrays;
import java.util.Collection;

public class ExpectationMaximizer {
    private LinearInterpolation interpolation;

    public ExpectationMaximizer(LinearInterpolation interpolation) {
        this.interpolation = interpolation;
    }

    public int trainLambdaValues(Collection<NGram> trainSentences, double epsilon, int maxSteps) {
        double[] newLambdas = new double[interpolation.lambdas.length];
        Arrays.fill(newLambdas, 1.0 / newLambdas.length);
        System.arraycopy(newLambdas, 0, interpolation.lambdas, 0, newLambdas.length);

        double[] modelInfluenceSum = new double[interpolation.lambdas.length];
        double dLambdas = 1;

        int step;
        for (step = 0; step < maxSteps && dLambdas > epsilon; step++) {
//            System.out.println("Step " + step + " lambdas = " + Arrays.toString(interpolation.lambdas) + "\n  diff from last step= " + dLambdas);

            Arrays.fill(modelInfluenceSum, 0);
            sumLambdaInfluences(trainSentences, modelInfluenceSum);
            normalize(modelInfluenceSum);

            System.arraycopy(modelInfluenceSum, 0, newLambdas, 0, newLambdas.length);

            dLambdas = compareAbsVectors(interpolation.lambdas, newLambdas);

            System.arraycopy(newLambdas, 0, interpolation.lambdas, 0, newLambdas.length);
        }


        return step;
    }

    private void sumLambdaInfluences(Collection<NGram> nGrams, double[] modelInfluenceSum) {
        double[] pk = new double[modelInfluenceSum.length];

        for (NGram nGram : nGrams) {
            double pli = 0;

            for (int j = 0; j < modelInfluenceSum.length; j++) {

                pk[j] = interpolation.models[j].getProbability(nGram);
                pli += interpolation.lambdas[j] * pk[j];

//                if (j == 1) {
//                    System.out.println("P(" + nGram[nGram.length - 1] + "|" + Arrays.toString(history) + ")=" + pk[j]);
//                }
            }

            if (pli == 0) {
                // this shouldn't happen with uniform model in place
                continue;
            }

            for (int j = 0; j < modelInfluenceSum.length; j++) {
                modelInfluenceSum[j] += pk[j] / pli;
            }
        }
    }

    private double compareAbsVectors(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.abs(a[i] - b[i]);
        }
        return sum;
    }

    /**
     * In-place value normalization
     *
     * @param array
     */
    private void normalize(double[] array) {
        double total = 0;
        for (double v : array) {
            total += v;
        }

        for (int i = 0; i < array.length; i++) {
            array[i] /= total;
        }
    }
}
