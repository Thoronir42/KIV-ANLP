package cz.zcu.students.kiwi.anlp.fileIO;

import java.util.stream.Stream;


public interface ProviderToolkit extends WordSetProvider {

    void reload();

    Stream<String[]> getParallelStream();

    void shuffle();
}
