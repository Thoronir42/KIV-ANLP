package cz.zcu.kiv.anlp.tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.stream.Stream;

public class BasicDataProvider implements DataProvider {

	private BufferedReader reader;
	private String fileName;
	
	public BasicDataProvider(String fileName) throws UnsupportedEncodingException, FileNotFoundException {
		this.fileName = fileName;
		this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
	}

	
	@Override
	public String[] next() throws IOException {
		String line = reader.readLine();
		if (line == null) return null;
		
		String[] words = line.split("[ \\t\\s\\n]+");
		return words;
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	@Override
	public void reload() throws IOException {
		this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
	}

	@Override
	public Stream<String[]> getParallelStream() {
		return null;
	}

	@Override
	public void shuffle() {

	}

}
