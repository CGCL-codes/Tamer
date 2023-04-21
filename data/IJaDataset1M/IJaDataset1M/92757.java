package com.folderdiff;

import java.io.File;
import java.io.FileWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author satyam
 */
public class DiffEngineTest {

    public DiffEngineTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of diffFile method, of class DiffEngine.
     */
    @Test
    public void testDiffFile() throws Exception {
        File f1 = new File("temp.txt");
        File f2 = new File("temp1.txt");
        boolean result = DiffEngine.diffFile("temp.txt", "temp1.txt");
        boolean expResult = true;
        assertEquals(expResult, result);
        FileWriter writer1 = new FileWriter(f1);
        FileWriter writer2 = new FileWriter(f2);
        writer1.write("abc");
        writer2.write("abc");
        expResult = true;
        result = DiffEngine.diffFile("temp.txt", "temp1.txt");
        assertEquals(expResult, result);
        writer1.write("abc");
        writer2.write("abc\ndef");
        writer1.close();
        writer2.close();
        expResult = false;
        result = DiffEngine.diffFile("temp.txt", "temp1.txt");
        assertEquals(expResult, result);
    }
}
