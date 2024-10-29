package ru.vrannir.reflection;

import ru.vrannir.reflection.annotations.AfterSuite;
import ru.vrannir.reflection.annotations.BeforeSuite;
import ru.vrannir.reflection.annotations.Test;
import ru.vrannir.reflection.exceptions.TestAnnotationException;
import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    private static final String TooManyBeforeSuiteMessage = "Too many BeforeSuite annotations!";
    private static final String TooManyAfterSuiteMessage = "Too many AfterSuite annotations!";
    private static final String MoreThanOneAnnotation = "Method has more than one annotation";

    public static void run(Class testClass) throws TestAnnotationException {

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> testMethods = new ArrayList<>();

        int successCounter = 0;
        int failCounter = 0;


        for (Method method : testClass.getDeclaredMethods()) {
            boolean haveAnnotation = false;
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
                haveAnnotation = true;
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if(haveAnnotation) throw new TestAnnotationException(MoreThanOneAnnotation);
                if(beforeSuiteMethod!=null){
                    throw new TestAnnotationException(TooManyBeforeSuiteMessage);
                }
                beforeSuiteMethod = method;
                haveAnnotation = true;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if(haveAnnotation) throw new TestAnnotationException(MoreThanOneAnnotation);
                if(afterSuiteMethod != null){
                    throw new TestAnnotationException(TooManyAfterSuiteMessage);
                }
                afterSuiteMethod = method;
            }
        }

        testMethods.sort((o1, o2) -> o2.getDeclaredAnnotation(Test.class).priority() - o1.getDeclaredAnnotation(Test.class).priority());
        if(beforeSuiteMethod != null) {
            testMethods.add(0, beforeSuiteMethod);
        }
        if(afterSuiteMethod != null) {
            testMethods.add(afterSuiteMethod);
        }
        for (Method method : testMethods) {
            try {
                method.invoke(null);
                successCounter ++;
            } catch (Exception e) {
                failCounter ++;
                System.out.println("method "+method.getName()+" has failed");
            }
        }
        System.out.println("Number of tests:" + testMethods.size());
        System.out.println("Tests succeeded:" + successCounter);
        System.out.println("Tests failed:" + failCounter);
    }
}
