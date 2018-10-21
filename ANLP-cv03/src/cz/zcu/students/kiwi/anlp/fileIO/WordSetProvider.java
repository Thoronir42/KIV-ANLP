package cz.zcu.students.kiwi.anlp.fileIO;

import java.io.IOException;


public interface WordSetProvider extends AutoCloseable {

    /**
     * cte vety az do doby co vrati null
     *
     * @return
     * @throws IOException
     */
    String[] next() throws IOException;

}
