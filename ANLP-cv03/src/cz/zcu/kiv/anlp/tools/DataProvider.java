package cz.zcu.kiv.anlp.tools;

import java.io.IOException;
import java.util.stream.Stream;


public interface DataProvider extends AutoCloseable{

	/**
	 * cte vety az do doby co vrati null
	 * @throws IOException
	 */
	public String[] next() throws IOException;

	/**
	 * zavrit na konci
	 * @throws IOException
	 */
	public void close() throws IOException;

	void reload() throws IOException;

	Stream<String[]> getParallelStream();

	void shuffle();
}
