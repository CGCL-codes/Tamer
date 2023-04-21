package org.demis.dwarf.database.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.demis.dwarf.database.DataBaseSchema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @version 1.0
 * @author <a href="mailto:demis27@demis27.net">Stéphane kermabon</a>
 */
public class DDLHandler extends RecursiveHandler {

    private static final Log logger = LogFactory.getLog(DDLHandler.class);

    private SchemaHandler schemaHandler = new SchemaHandler();

    private TableHandler tableHandler = null;

    private DefaultHandler currentHandler = null;

    @Override
    public void startDocument() throws SAXException {
        currentHandler = schemaHandler;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        logger.info("DDLHandler.endElement(" + uri + ", " + localName + ", " + qName + ")");
        if ("schema".equalsIgnoreCase(qName)) {
            currentHandler.endElement(uri, localName, qName);
        } else if ("table".equalsIgnoreCase(qName)) {
            tableHandler.endElement(uri, localName, qName);
            schemaHandler.getSchema().addTable(tableHandler.getTable());
            currentHandler = schemaHandler;
            tableHandler = null;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        logger.info("DDLHandler.startElement(" + uri + ", " + localName + ", " + qName + ", ...");
        if ("schema".equalsIgnoreCase(qName)) {
            currentHandler.startElement(uri, localName, qName, attributes);
        } else if ("table".equalsIgnoreCase(qName)) {
            tableHandler = new TableHandler();
            tableHandler.startElement(uri, localName, qName, attributes);
        }
    }

    public DataBaseSchema getSchema() {
        return schemaHandler.getSchema();
    }
}
