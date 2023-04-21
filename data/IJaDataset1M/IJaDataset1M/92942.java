package montreal.demo.it;

import java.net.HttpURLConnection;
import java.net.URL;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WebappTest extends TestCase {

    public void testCallIndexPage() throws Exception {
        URL url = new URL("http://localhost:8080/cargo");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(WebappTest.class);
        return new CargoTestSetup(suite);
    }
}
