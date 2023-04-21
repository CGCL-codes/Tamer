package org.exist.protocolhandler.xmldb;

import java.net.MalformedURLException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *  jUnit tests for the XmldbURL class.
 *
 * @author Dannes Wessels
 */
public class RemoteXmldbURLTest {

    private static Logger LOG = Logger.getLogger(EmbeddedXmldbURLTest.class);

    private static String XMLDB_URL_0 = "xmldb:exist://username:password@localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_1 = "xmldb:exist://guest:guest@localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_2 = "xmldb:exist://guest@localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_3 = "xmldb:exist://@localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_4 = "xmldb:exist://:@localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_5 = "xmldb:exist://localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_11 = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";

    private static String XMLDB_URL_12 = "xmldb:exist://localhost:8080/exist/xmlrpc/db";

    private static String XMLDB_URL_13 = "xmldb:exist://localhost:8080/exist/xmlrpc/";

    private static String XMLDB_URL_14 = "xmldb:exist://localhost:8080/exist/xmlrpc";

    private static String XMLDB_URL_15 = "xmldb:exist://localhost:8080/exist";

    private static String XMLDB_URL_21 = "xmldb:exist://localhost:8080/exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    private static String XMLDB_URL_22 = "xmldb:exist:///exist/xmlrpc" + "/db/shakespeare/plays/macbeth.xml";

    @BeforeClass
    public static void start() throws Exception {
        PropertyConfigurator.configure("log4j.conf");
        System.setProperty("java.protocol.handler.pkgs", "org.exist.protocolhandler.protocols");
    }

    /**
     * Test of getUserInfo method, of class org.exist.protocolhandler.protocols.util.XmldbURL.
     */
    @Test
    public void testGetUserInfo() {
        System.out.println("testGetUserInfo");
        try {
            XmldbURL xmldbUrl = new XmldbURL(XMLDB_URL_0);
            assertEquals("username:password", xmldbUrl.getUserInfo());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of getUsername method, of class org.exist.protocolhandler.protocols.util.XmldbURL.
     */
    @Test
    public void testGetUsername() {
        System.out.println("testGetUsername");
        try {
            XmldbURL xmldbUrl = new XmldbURL(XMLDB_URL_0);
            assertEquals("username", xmldbUrl.getUsername());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of getPassword method, of class org.exist.protocolhandler.protocols.util.XmldbURL.
     */
    @Test
    public void testGetPassword() {
        System.out.println("testGetPassword");
        try {
            XmldbURL xmldbUrl = new XmldbURL(XMLDB_URL_0);
            assertEquals("password", xmldbUrl.getPassword());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of getCollection method, of class org.exist.protocolhandler.protocols.util.XmldbURL.
     */
    @Test
    public void testGetCollection() {
        System.out.println("testGetCollection");
        try {
            XmldbURL xmldbUrl = new XmldbURL(XMLDB_URL_0);
            assertEquals("/db/shakespeare/plays", xmldbUrl.getCollection());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of getDocumentName method, of class org.exist.protocolhandler.protocols.util.XmldbURL.
     */
    @Test
    public void testGetDocumentName() {
        System.out.println("testGetDocumentName");
        try {
            XmldbURL xmldbUrl = new XmldbURL(XMLDB_URL_0);
            assertEquals("macbeth.xml", xmldbUrl.getDocumentName());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
        }
    }

    @Test
    public void testMoreOnOneXmldbURL() {
        System.out.println("testMoreOnOneXmldbURL");
        try {
            XmldbURL url = new XmldbURL(XMLDB_URL_1);
            assertEquals("xmldb", url.getProtocol());
            assertEquals("guest:guest", url.getUserInfo());
            assertEquals("localhost", url.getHost());
            assertEquals(8080, url.getPort());
            assertEquals("/exist/xmlrpc/db/shakespeare/plays/macbeth.xml", url.getPath());
            assertNull(url.getQuery());
            assertEquals("/db/shakespeare/plays/macbeth.xml", url.getCollectionPath());
            assertEquals("/exist/xmlrpc", url.getContext());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
            LOG.error(ex);
        }
    }

    @Test
    public void testXmldbURI_getUserInfo() {
        System.out.println("testXmldbURI_getUserInfo");
        try {
            String userinfo = null;
            XmldbURL url = new XmldbURL(XMLDB_URL_1);
            userinfo = url.getUserInfo();
            assertEquals("guest:guest", userinfo);
            assertTrue(!"foo:bar".equals(userinfo));
            url = new XmldbURL(XMLDB_URL_1);
            userinfo = url.getUserInfo();
            assertEquals("guest", url.getUsername());
            assertEquals("guest", url.getPassword());
            url = new XmldbURL(XMLDB_URL_2);
            userinfo = url.getUserInfo();
            assertEquals("guest", userinfo);
            assertEquals("guest", url.getUsername());
            assertNull(url.getPassword());
            url = new XmldbURL(XMLDB_URL_3);
            userinfo = url.getUserInfo();
            assertEquals("", userinfo);
            assertNull(url.getUsername());
            assertNull(url.getPassword());
            url = new XmldbURL(XMLDB_URL_4);
            userinfo = url.getUserInfo();
            assertEquals(":", userinfo);
            assertNull(url.getUsername());
            assertNull(url.getPassword());
            url = new XmldbURL(XMLDB_URL_5);
            userinfo = url.getUserInfo();
            assertNull(userinfo);
            assertNull(url.getUsername());
            assertNull(url.getPassword());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
            LOG.error(ex);
        }
    }

    @Test
    public void testXmldbURI_HostName() {
        System.out.println("testXmldbURI_HostName");
        try {
            XmldbURL url = new XmldbURL(XMLDB_URL_21);
            assertEquals("localhost", url.getHost());
            url = new XmldbURL(XMLDB_URL_22);
            assertNull(url.getHost());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
            LOG.error(ex);
        }
    }

    @Test
    public void testXmldbURI_InstanceName() {
        System.out.println("testXmldbURI_InstanceName");
        try {
            XmldbURL url = new XmldbURL(XMLDB_URL_1);
            assertEquals("exist", url.getInstanceName());
        } catch (MalformedURLException ex) {
            fail(ex.getMessage());
            LOG.error(ex);
        }
    }
}
