package backend.query.test;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Hashtable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import junit.framework.TestCase;
import backend.core.AbstractConcept;
import backend.core.AbstractONDEXGraph;
import backend.core.persistent.perst.PerstEnv;
import backend.core.persistent.test.DirUtils;
import backend.core.security.Session;
import backend.exchange.xml.ConceptParser;
import backend.exchange.xml.RelationParser;
import backend.exchange.xml.XmlParser;
import backend.logging.ONDEXLogger;
import backend.query.Query;
import backend.query.QueryEnv;

/**
 * This class tests the ConceptQuery on persistent database.
 * @author sierenk
 *
 */
public class ConceptQueryTest extends TestCase {

    private QueryEnv qenv;

    private PerstEnv perst;

    private static final Session s = Session.NONE;

    private static final String testdataXML = System.getProperty("ondex.dir") + File.separator + "xml" + File.separator + "testConcepts.xml";

    private static final String ondexName = "ConceptQueryTest";

    private static final String filenameDB = System.getProperty("ondex.dir") + File.separator + "dbs" + File.separator + ondexName + File.separator + "ondex.dbs";

    protected void setUp() throws Exception {
        DirUtils.deleteTree(filenameDB);
        perst = new PerstEnv(filenameDB, new ONDEXLogger());
        AbstractONDEXGraph og = perst.getONDEXGraph();
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileReader(testdataXML));
        Hashtable<Integer, Integer> idOldNew = new Hashtable<Integer, Integer>();
        XmlParser parser = new XmlParser();
        parser.registerParser("concept", new ConceptParser(s, og, idOldNew));
        parser.registerParser("relation", new RelationParser(s, og, idOldNew));
        parser.parse(xmlr);
        xmlr.close();
        perst.cleanup();
        perst = null;
        qenv = new QueryEnv(s, filenameDB, true);
    }

    protected void tearDown() throws Exception {
        qenv.cleanup();
        qenv = null;
        DirUtils.deleteTree(filenameDB);
    }

    /**
	 * Tests queries for all preferred concept names.
	 * @throws Exception
	 */
    public void testConceptNameQuery() throws Exception {
        long start = System.currentTimeMillis();
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisConceptName("norway").values();
        assertEquals(1, result.size());
        assertEquals("Norway Spruce", result.iterator().next().getConceptName(s).getName(s));
        Collection<AbstractConcept> result2 = query.queryForThisConceptName("abies").values();
        assertEquals(2, result2.size());
        assertTrue(result2.contains(qenv.getGraph().getConcept(s, 1)));
        assertTrue(result2.contains(qenv.getGraph().getConcept(s, 2)));
        Collection<AbstractConcept> result3 = query.queryForThisConceptName("European beech").values();
        assertEquals(1, result3.size());
        assertEquals("European beech", result3.iterator().next().getConceptName(s).getName(s));
        Collection<AbstractConcept> result4 = query.queryForThisConceptName("Pine family").values();
        assertEquals(1, result4.size());
        assertEquals("Pine family", result4.iterator().next().getConceptName(s).getName(s));
        System.out.println("  testConceptNameQuery() took " + (System.currentTimeMillis() - start) + " msec.");
    }

    /**
	 * Tests queries forconcept classes.
	 * @throws Exception
	 */
    public void testConceptClassQuery() throws Exception {
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisConceptClass("TAXID").values();
        assertEquals(5, result.size());
        Collection<AbstractConcept> result2 = query.queryForThisConceptClass("Test").values();
        assertEquals(1, result2.size());
    }

    /**
	 * Tests queries for CVs.
	 * @throws Exception
	 */
    public void testConceptCVQuery() throws Exception {
        long start = System.currentTimeMillis();
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisCV("Taxonomy").values();
        assertEquals(6, result.size());
        Collection<AbstractConcept> result2 = query.queryForThisCV("Test").values();
        assertEquals(0, result2.size());
        System.out.println("  testConceptCVQuery() took " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testQueryForCommaSeparatedCVsQuery() throws Exception {
        long start = System.currentTimeMillis();
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisCommaSeparatedCVs("Taxonomy").values();
        assertEquals(6, result.size());
        Collection<AbstractConcept> result2 = query.queryForThisCommaSeparatedCVs("Test").values();
        assertEquals(0, result2.size());
        Collection<AbstractConcept> result3 = query.queryForThisCommaSeparatedCVs("Taxonomy,").values();
        assertEquals(6, result3.size());
        Collection<AbstractConcept> result4 = query.queryForThisCommaSeparatedCVs("Taxonomy,Test,bla,xy").values();
        assertEquals(6, result4.size());
        System.out.println("  testQueryForCommaSeparatedCVsQuery() took " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testQueryForThisCommaSeparatedConceptClasses() throws Exception {
        long start = System.currentTimeMillis();
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisCommaSeparatedConceptClasses("").values();
        assertEquals(0, result.size());
        Collection<AbstractConcept> result2 = query.queryForThisCommaSeparatedConceptClasses("Test").values();
        assertEquals(1, result2.size());
        Collection<AbstractConcept> result3 = query.queryForThisCommaSeparatedConceptClasses("TAXID,").values();
        assertEquals(5, result3.size());
        Collection<AbstractConcept> result4 = query.queryForThisCommaSeparatedConceptClasses(",Test,TAXID,").values();
        assertEquals(6, result4.size());
        Collection<AbstractConcept> result5 = query.queryForThisCommaSeparatedConceptClasses("Test,TAXID,").values();
        assertEquals(6, result5.size());
        System.out.println("  testQueryForCommaSeparatedConceptClasses() took " + (System.currentTimeMillis() - start) + " msec.");
    }

    /**
	 * Tests queries for all ConceptGDs.
	 * @throws Exception
	 */
    public void testConceptGDSQuery() throws Exception {
        long start = System.currentTimeMillis();
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisConceptGDS("Taxonomy ID", "45372").values();
        assertEquals(1, result.size());
        assertEquals("Christmas-tree", result.iterator().next().getConceptName(s).getName(s));
        assertEquals(45372, result.iterator().next().getConceptGDS(s, qenv.getGraph().getONDEXGraphData(s).getAttributeName(s, "Taxonomy ID")).getValue(s));
        Collection<AbstractConcept> result2 = query.queryForThisConceptGDSValue("45372").values();
        assertEquals(1, result2.size());
        assertEquals("Christmas-tree", result2.iterator().next().getConceptName(s).getName(s));
        assertEquals(45372, result2.iterator().next().getConceptGDS(s, qenv.getGraph().getONDEXGraphData(s).getAttributeName(s, "Taxonomy ID")).getValue(s));
        Collection<AbstractConcept> result3 = query.queryForThisConceptGDSValue("Santa Claus").values();
        assertEquals(1, result3.size());
        assertEquals("Christmas-tree", result3.iterator().next().getConceptName(s).getName(s));
        assertEquals("Santa Claus", result3.iterator().next().getConceptGDS(s, qenv.getGraph().getONDEXGraphData(s).getAttributeName(s, "Mery Christmas")).getValue(s));
        Collection<AbstractConcept> result4 = query.queryForThisConceptGDS("Mery Christmas", "Santa Claus").values();
        assertEquals(1, result4.size());
        assertEquals("Christmas-tree", result4.iterator().next().getConceptName(s).getName(s));
        assertEquals("Santa Claus", result4.iterator().next().getConceptGDS(s, qenv.getGraph().getONDEXGraphData(s).getAttributeName(s, "Mery Christmas")).getValue(s));
        Collection<AbstractConcept> result5 = query.queryForThisConceptGDS("Taxonomy ID", "3329").values();
        assertEquals(1, result5.size());
        assertEquals("Norway Spruce", result5.iterator().next().getConceptName(s).getName(s));
        assertEquals(3329, result5.iterator().next().getConceptGDS(s, qenv.getGraph().getONDEXGraphData(s).getAttributeName(s, "Taxonomy ID")).getValue(s));
        System.out.println("  testConceptGDSQuery()took " + (System.currentTimeMillis() - start) + " msec.");
    }

    public void testRelationGDSNameQuery() throws Exception {
        Query query = qenv.getQuery();
        Collection<AbstractConcept> result = query.queryForThisConceptGDSName("Mery Christmas").values();
        assertEquals(1, result.size());
        result = query.queryForThisConceptGDSName("Taxonomy ID").values();
        assertEquals(4, result.size());
    }
}
