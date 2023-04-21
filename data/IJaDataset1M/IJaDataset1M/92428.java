package preprocess;

import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Compaq
 */
public class StemmerWithStopWordRemoverTest {

    public StemmerWithStopWordRemoverTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of analyze method, of class StemmerWithStopWordRemover.
     */
    @Test
    public void testAnalyze() throws Exception {
        System.out.println("analyze");
        String text = "First we will study plagiarism patterns Techniques can be found in the context of our scope";
        StemmerWithStopWordRemover instance = new StemmerWithStopWordRemover();
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("first");
        expResult.add("we");
        expResult.add("studi");
        expResult.add("plagiar");
        expResult.add("pattern");
        expResult.add("techniqu");
        expResult.add("can");
        expResult.add("found");
        expResult.add("context");
        expResult.add("our");
        expResult.add("scope");
        ArrayList<String> result = instance.analyze(text);
        System.out.println(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of analyze method, of class StemmerWithStopWordRemover.
     */
    @Test
    public void testAnalyze_String() throws Exception {
        System.out.println("analyze");
        String text = "";
        StemmerWithStopWordRemover instance = new StemmerWithStopWordRemover();
        ArrayList<String> expResult = null;
        ArrayList<String> result = instance.analyze(text);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of analyzenew method, of class StemmerWithStopWordRemover.
     */
    @Test
    public void testAnalyzenew() throws Exception {
        System.out.println("analyzenew");
        String text = "";
        StemmerWithStopWordRemover instance = new StemmerWithStopWordRemover();
        ArrayList expResult = null;
        ArrayList result = instance.analyzenew(text);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
