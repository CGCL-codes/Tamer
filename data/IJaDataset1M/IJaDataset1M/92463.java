package test;

import java.util.ArrayList;
import java.util.List;

public class TestRegEx {

    /**
     * @param args
     */
    public static void main(String[] args) {
        String[] a = { "A1", "A2", "A3" };
        List<String> b = new ArrayList<String>();
        b.add("B1");
        b.add("B2");
        b.add("B3");
        for (String str : a) {
            System.out.println(str);
        }
        for (String str : b) {
            System.out.println(str);
        }
    }
}
