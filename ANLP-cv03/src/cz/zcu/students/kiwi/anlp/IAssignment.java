package cz.zcu.students.kiwi.anlp;

import java.io.InputStream;
import java.io.PrintStream;

public interface IAssignment {
    void run(PrintStream output, InputStream inputStream) throws Exception;
}
