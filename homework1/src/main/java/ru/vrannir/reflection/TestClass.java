package ru.vrannir.reflection;

import ru.vrannir.reflection.annotations.AfterSuite;
import ru.vrannir.reflection.annotations.BeforeSuite;
import ru.vrannir.reflection.annotations.Test;

public class TestClass {

    @BeforeSuite
    public static void beforeTest() {
        System.out.println("beforeTest running!");
    }

    @Test(priority = 10)
    public static void test1() {
        System.out.println("test1 is running!");
    }

    @AfterSuite
    public static void afterTest() {
        System.out.println("afterTest is running!");
    }

    @Test(priority = 7)
    public static void test2() {
        System.out.println("test2 is running!");
    }

    @Test
    public static void test3() {
        int a = 10;
        int b = 0;
        int c = a / b;
        System.out.println("!" + c);
    }

    //@AfterSuite
    public static void beforeTest2() {
        System.out.println("beforeTest2 is running!");
    }
}
