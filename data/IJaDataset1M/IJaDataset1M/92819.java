package net.sourceforge.cruisecontrol.dashboard.utils;

import java.io.IOException;
import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.util.Commandline;

public class PipeTest extends TestCase {

    public void testShouldThrowExecutionExceptionWhenExceptionIsThrownOut() {
        try {
            new Pipe(new MockCommandLine());
            fail();
        } catch (Exception e) {
        }
    }

    class MockCommandLine extends Commandline {

        public Process execute() throws IOException {
            throw new IOException();
        }
    }
}
