package test.org.mbari.vars.annotation.io;

import java.io.IOException;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.mbari.vars.annotation.io.VifInput;

/**
 * @author brian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class VifInputTest extends TestCase {

    public VifInputTest(String arg0) {
        super(arg0);
        vifUrl = getClass().getResource("/test/2004029WVO.vif");
    }

    public static Test suite() {
        return new TestSuite(VifInputTest.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    public void test1() {
        assertNotNull("Unable to find /test/2004029WVO.vif", vifUrl);
    }

    public void test2() {
        VifInput vi = new VifInput(vifUrl);
        assertNotNull(vi);
        try {
            vi.read();
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private final URL vifUrl;
}
