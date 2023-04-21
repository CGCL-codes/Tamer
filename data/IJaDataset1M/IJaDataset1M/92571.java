package cc.mallet.pipe.tests;

import cc.mallet.fst.SimpleTagger;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PipeUtils;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.SimpleTaggerSentence2TokenSequence;
import cc.mallet.types.Alphabet;
import cc.mallet.types.Instance;
import junit.framework.*;

/**
 * Created: Aug 28, 2005
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: TestPipeUtils.java,v 1.1 2007/10/22 21:37:40 mccallum Exp $
 */
public class TestPipeUtils extends TestCase {

    public TestPipeUtils(String name) {
        super(name);
    }

    private static class StupidPipe extends Pipe {

        public Instance pipe(Instance carrier) {
            System.out.println("StupidPipe says hi.");
            return carrier;
        }
    }

    private static String data = "f1 f2 CL1\nf1 f3 CL2";

    public void testPipesAreStupid() {
        Pipe p1 = new StupidPipe();
        Pipe p2 = new SimpleTaggerSentence2TokenSequence();
        p2.instanceFrom(new Instance(data, null, null, null));
        Pipe serial = new SerialPipes(new Pipe[] { p1, p2 });
        try {
            serial.getDataAlphabet();
            assertTrue("Test failed: Should have generated exception.", false);
        } catch (IllegalStateException e) {
        }
    }

    public void testConcatenatePipes() {
        Pipe p1 = new StupidPipe();
        Pipe p2 = new SimpleTagger.SimpleTaggerSentence2FeatureVectorSequence();
        p2.instanceFrom(new Instance(data, null, null, null));
        assertEquals(3, p2.getDataAlphabet().size());
        Pipe serial = PipeUtils.concatenatePipes(p1, p2);
        Alphabet dict = serial.getDataAlphabet();
        assertEquals(3, dict.size());
        assertTrue(dict == p2.getDataAlphabet());
    }

    public void testConcatenateNullPipes() {
        Pipe p1 = new StupidPipe();
        Pipe p2 = new SimpleTagger.SimpleTaggerSentence2FeatureVectorSequence();
        Pipe serial = PipeUtils.concatenatePipes(p1, p2);
        p2.instanceFrom(new Instance(data, null, null, null));
        assertEquals(3, serial.getDataAlphabet().size());
    }

    public void testConcatenateBadPipes() {
        Pipe p1 = new SimpleTaggerSentence2TokenSequence();
        Alphabet dict1 = p1.getDataAlphabet();
        Pipe p2 = new SimpleTaggerSentence2TokenSequence();
        Alphabet dict2 = p2.getDataAlphabet();
        assertTrue(dict1 != dict2);
        try {
            PipeUtils.concatenatePipes(p1, p2);
            assertTrue("Test failed: concatenatePipes() allowed putting together incompatible alphabets.", false);
        } catch (IllegalArgumentException e) {
        }
    }

    public static Test suite() {
        return new TestSuite(TestPipeUtils.class);
    }

    public static void main(String[] args) throws Throwable {
        TestSuite theSuite;
        if (args.length > 0) {
            theSuite = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                theSuite.addTest(new TestPipeUtils(args[i]));
            }
        } else {
            theSuite = (TestSuite) suite();
        }
        junit.textui.TestRunner.run(theSuite);
    }
}
