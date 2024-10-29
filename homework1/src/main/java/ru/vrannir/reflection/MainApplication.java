package ru.vrannir.reflection;

import ru.vrannir.reflection.exceptions.TestAnnotationException;


public class MainApplication {
    public static void main(String[] args) {
        try {
            TestRunner.run(TestClass.class);
        } catch (TestAnnotationException e) {
            System.out.println(e.getMessage());
        }
    }
}
