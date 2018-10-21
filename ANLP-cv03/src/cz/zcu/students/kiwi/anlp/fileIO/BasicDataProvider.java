package cz.zcu.students.kiwi.anlp.fileIO;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BasicDataProvider implements WordSetProvider {

    private BufferedReader reader;

    public BasicDataProvider(String file, Charset charset) throws FileNotFoundException {
        this(new InputStreamReader(new FileInputStream(file), charset));
    }

    public BasicDataProvider(InputStreamReader inputStream) {
        this.reader = new BufferedReader(inputStream);
    }


    @Override
    public String[] next() throws IOException {
        String line = reader.readLine();
        if (line == null) return null;

        String[] words = line.split("[\\s,]+");
        if (words.length < 1) {
            return null;
        }
        return words;
    }

    public List<String[]> readAll() throws IOException {
        List<String[]> sentences = new ArrayList<>();
        String[] sentence;
        while ((sentence = this.next()) != null) {
            sentences.add(sentence);
        }

        return sentences;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }


    public static List<String[]> readAll(String file, Charset charset) throws IOException {
        return new BasicDataProvider(file, charset).readAll();
    }

}
