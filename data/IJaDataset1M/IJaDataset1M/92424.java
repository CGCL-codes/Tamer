package test;

public class ExtractMethodTester3 {

    public static void go() {
        int x = 4;
        for (int i = 0; i < 10; i++) {
            x++;
        }
        System.out.println("this is fun!");
    }

    public static void stop() {
        int x = 4;
        x = foo(x);
        System.out.println("the value of x is " + x);
    }

    public void yield() {
        int x = 4;
        int y = 2;
        for (int i = 0; i < 10; i++) {
            x++;
            y--;
        }
        System.out.println("the value of x is " + x);
        System.out.println("the value of y is " + y);
    }

    public static int foo(int x) {
        for (int i = 0; i < 10; i++) {
            x++;
        }
        return x;
    }
}
