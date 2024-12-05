package ru.vrannir.reflection;

import ru.vrannir.reflection.annotations.AfterSuite;
import ru.vrannir.reflection.annotations.BeforeSuite;
import ru.vrannir.reflection.annotations.Test;
import ru.vrannir.reflection.exceptions.TestAnnotationException;

import java.lang.reflect.Method;
import java.util.*;

public class TestRunner {

    private static final String TOO_MANY_BEFORE_SUITE_MESSAGE = "Too many BeforeSuite annotations!";
    private static final String TOO_MANY_AFTER_SUITE_MESSAGE = "Too many AfterSuite annotations!";
    private static final String MORE_THAN_ONE_ANNOTATION_MESSAGE = "Method has more than one annotation";
    private static final String OUTSIDE_RANGE_MESSAGE = "Priority not in 1..10 range";

    private static boolean executeMethod(Method method) {
        try {
            method.invoke(null);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static void run(Class testClass) throws TestAnnotationException {

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        List<Method> testMethods = new ArrayList<>();

        int successCounter = 0;
        int failCounter = 0;


        for (Method method : testClass.getDeclaredMethods()) {
            boolean haveAnnotation = false;
            if (method.isAnnotationPresent(Test.class)) {
                if (method.getDeclaredAnnotation(Test.class).priority() < 1 || method.getDeclaredAnnotation(Test.class).priority() > 10) {
                    throw new TestAnnotationException(OUTSIDE_RANGE_MESSAGE);
                }
                testMethods.add(method);
                haveAnnotation = true;
            }
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                if (haveAnnotation) throw new TestAnnotationException(MORE_THAN_ONE_ANNOTATION_MESSAGE);
                if (beforeSuiteMethod != null) {
                    throw new TestAnnotationException(TOO_MANY_BEFORE_SUITE_MESSAGE);
                }
                beforeSuiteMethod = method;
                haveAnnotation = true;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (haveAnnotation) throw new TestAnnotationException(MORE_THAN_ONE_ANNOTATION_MESSAGE);
                if (afterSuiteMethod != null) {
                    throw new TestAnnotationException(TOO_MANY_AFTER_SUITE_MESSAGE);
                }
                afterSuiteMethod = method;
            }
        }

        testMethods.sort((o1, o2) -> o2.getDeclaredAnnotation(Test.class).priority() - o1.getDeclaredAnnotation(Test.class).priority());
        if (beforeSuiteMethod != null) {
            if (!executeMethod(beforeSuiteMethod)) {
                System.out.println("method " + beforeSuiteMethod.getName() + " has failed");
            }
        }
        for (Method method : testMethods) {
            if (executeMethod(method)) successCounter++;
            else {
                failCounter++;
                System.out.println("method " + method.getName() + " has failed");
            }
        }
        if (afterSuiteMethod != null) {
            if (!executeMethod(afterSuiteMethod)) {
                System.out.println("method " + afterSuiteMethod.getName() + " has failed");
            }
        }
        System.out.println("Number of tests:" + testMethods.size());
        System.out.println("Tests succeeded:" + successCounter);
        System.out.println("Tests failed:" + failCounter);
    }
}
