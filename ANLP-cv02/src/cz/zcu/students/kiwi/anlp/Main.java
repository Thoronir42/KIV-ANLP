package cz.zcu.students.kiwi.anlp;

import cz.zcu.students.kiwi.anlp.a02.Assignment_02;

public class Main {

    public static void main(String[] args) {
        IAssignment assignment = new Assignment_02();

        try {
            assignment.run(System.out, System.in);
        } catch (Exception e) {
            System.err.println("Error occured during runtime of " + assignment.getClass().getName());
            e.printStackTrace();
        }
    }
}
