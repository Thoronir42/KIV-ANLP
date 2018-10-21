package cz.zcu.students.kiwi.anlp.fileIO;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class WordSetProviderToolkit implements ProviderToolkit, AutoCloseable {

    private BasicWordSetProvider provider;

    public WordSetProviderToolkit(BasicWordSetProvider provider) {
        this.provider = provider;
    }

    @Override
    public void reload() {
        if (provider.file == null) {
            throw new UnsupportedOperationException("Word set provider must have a file associated to be reload()-able");
        }
        try {
            provider.reader = new BufferedReader(new InputStreamReader(new FileInputStream(provider.file)));
        } catch (IOException ex) {
            // fixme: yuck
            throw new RuntimeException("Failed to reload provider", ex);
        }
    }

    @Override
    public Stream<String[]> getParallelStream() {
        return provider.stream().parallel();
    }

    @Override
    public void shuffle() {

    }

    @Override
    public String[] next() {
        return provider.next();
    }

    @Override
    public void close() throws Exception {
        provider.close();
    }
}
