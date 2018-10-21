package cz.zcu.students.kiwi.anlp.fileIO;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BasicWordSetProvider implements WordSetProvider, AutoCloseable {

    String file;
    BufferedReader reader;

    public BasicWordSetProvider(String file, Charset charset) throws FileNotFoundException {
        this(new FileInputStream(file), charset);
        this.file = file;
    }

    public BasicWordSetProvider(InputStream inputStream, Charset charset) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream, charset));
    }


    @Override
    public String[] next() {
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line == null) return null;

        return splitLine(line);
    }

    private String[] splitLine(String line) {
        String[] words = line.split("[\\s,]+");
        if (words.length < 1) {
            return null;
        }

        return words;
    }

    public Stream<String[]> stream() {
        return this.reader.lines().map(this::splitLine);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }


    public static Stream<String[]> stream(String file, Charset charset) throws IOException {
        return new BasicWordSetProvider(file, charset).stream();
    }
}
