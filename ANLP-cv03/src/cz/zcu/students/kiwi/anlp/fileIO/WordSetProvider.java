package cz.zcu.students.kiwi.anlp.fileIO;


public interface WordSetProvider {

    /**
     * cte vety az do doby co vrati null
     *
     * @return Sentence split to words/tokens
     */
    String[] next();

}
