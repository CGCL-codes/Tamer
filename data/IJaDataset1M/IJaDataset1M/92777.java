package org.fcrepo.test.api;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.fcrepo.client.FedoraClient;
import org.fcrepo.client.HttpInputStream;
import org.fcrepo.common.Models;
import org.fcrepo.test.DemoObjectTestSetup;
import org.fcrepo.test.FedoraServerTestCase;
import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Document;

/**
 * @author Edwin Shin
 */
public class TestAPIALite extends FedoraServerTestCase {

    private static FedoraClient client;

    public static Test suite() {
        TestSuite suite = new TestSuite("APIALite TestSuite");
        suite.addTestSuite(TestAPIALite.class);
        return new DemoObjectTestSetup(suite);
    }

    @Override
    @Before
    public void setUp() throws Exception {
        client = getFedoraClient();
        Map<String, String> nsMap = new HashMap<String, String>();
        nsMap.put(NS_FEDORA_TYPES_PREFIX, NS_FEDORA_TYPES);
        nsMap.put(OAI_DC.prefix, OAI_DC.uri);
        nsMap.put(DC.prefix, DC.uri);
        nsMap.put(ACCESS.prefix, ACCESS.uri);
        NamespaceContext ctx = new SimpleNamespaceContext(nsMap);
        XMLUnit.setXpathNamespaceContext(ctx);
    }

    @Override
    @After
    public void tearDown() {
        XMLUnit.setXpathNamespaceContext(SimpleNamespaceContext.EMPTY_CONTEXT);
    }

    public void testDescribeRepository() throws Exception {
        Document result;
        result = getXMLQueryResult("/describe?xml=true");
        assertXpathExists(String.format("/%s:fedoraRepository/%s:repositoryName", ACCESS.prefix, ACCESS.prefix), result);
    }

    public void testGetDatastreamDissemination() throws Exception {
        Document result;
        result = getXMLQueryResult("/get/demo:5/DC");
        assertXpathExists("/oai_dc:dc", result);
        assertXpathEvaluatesTo("demo:5", "/oai_dc:dc/dc:identifier/text()", result);
        HttpInputStream in = client.get("/get/demo:SmileyBeerGlass/MEDIUM_SIZE", true);
        assertEquals(in.getContentType(), "image/jpeg");
        assertTrue(in.getContentLength() > 0);
        in.close();
        in = client.get("/get/demo:31/DS3", false, false);
        assertEquals(in.getStatusCode(), 302);
        in.close();
        in = client.get("/get/demo:5/THUMBRES_IMG", true);
        assertEquals(in.getContentType(), "image/jpeg");
        in.close();
    }

    public void testGetDisseminationDefault() throws Exception {
        HttpInputStream his = client.get("/get/demo:5/fedora-system:3/viewDublinCore", true);
        assertEquals(his.getContentType(), "text/html");
        his.close();
    }

    public void testGetDisseminationUserInput() throws Exception {
        HttpInputStream his = client.get("/get/demo:29/demo:27/convertImage?convertTo=gif", true);
        assertEquals(his.getContentType(), "image/gif");
        his.close();
    }

    public void testObjectHistory() throws Exception {
        Document result;
        result = getXMLQueryResult("/getObjectHistory/demo:5?xml=true");
        assertXpathExists(String.format("/%s:fedoraObjectHistory/%s:objectChangeDate", ACCESS.prefix, ACCESS.prefix), result);
    }

    public void testGetObjectProfile() throws Exception {
        Document result;
        result = getXMLQueryResult("/get/demo:5?xml=true");
        assertXpathEvaluatesTo("demo:5", String.format("/%s:objectProfile/attribute::pid", ACCESS.prefix), result);
    }

    public void testGetObjectProfileBasicCModel() throws Exception {
        String testExpression = String.format("count(" + "/%s:objectProfile/%s:objModels/%s:model[normalize-space()='" + Models.FEDORA_OBJECT_CURRENT.uri + "'])", ACCESS.prefix, ACCESS.prefix, ACCESS.prefix);
        for (String pid : new String[] { "demo:SmileyPens", "demo:SmileyGreetingCard" }) {
            Document result = getXMLQueryResult("/get/" + pid + "?xml=true");
            assertXpathEvaluatesTo("1", testExpression, result);
        }
    }

    public void testListDatastreams() throws Exception {
        Document result;
        result = getXMLQueryResult("/listDatastreams/demo:5?xml=true");
        assertXpathEvaluatesTo("6", String.format("count(/%s:objectDatastreams/%s:datastream)", ACCESS.prefix, ACCESS.prefix), result);
    }

    public void testListMethods() throws Exception {
        Document result;
        result = getXMLQueryResult("/listMethods/demo:5?xml=true");
        assertXpathEvaluatesTo("8", String.format("count(/%s:objectMethods/%s:sDef/%s:method)", ACCESS.prefix, ACCESS.prefix, ACCESS.prefix), result);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TestAPIALite.class);
    }
}
