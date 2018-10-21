package cz.zcu.students.kiwi.anlp.markov;

import java.util.List;

public interface IMarkovModel {
    List<String> getBestSequence(List<String> words);
}
