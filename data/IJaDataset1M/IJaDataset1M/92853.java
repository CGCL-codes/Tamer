package net.sourceforge.jpalm.palmdb;

import java.io.FileInputStream;
import junit.framework.TestCase;
import net.sourceforge.jpalm.palmdb.PalmDB;
import net.sourceforge.jpalm.palmdb.PalmDBImpl;
import net.sourceforge.jpalm.palmdb.PalmReader;
import net.sourceforge.jpalm.palmdb.Record;
import net.sourceforge.jpalm.palmdb.RecordImpl;
import net.sourceforge.juint.UInt16;
import net.sourceforge.juint.UInt8;

public class TestPalmReader extends TestCase {

    protected static final String RESOURCE = "tests/net/sourceforge/jpalm/palmdb/resources/test.pdb";

    public void testLoad() throws Exception {
        PalmDB database = new PalmDBImpl();
        PalmReader reader = new PalmReader();
        FileInputStream stream = new FileInputStream(RESOURCE);
        reader.load(stream, database);
        assertTrue(database.getApplicationInfo().getCategoryLabels().contains("FieldLabels"));
        assertEquals("test", database.getHeader().getName());
        assertEquals(new UInt16(9), database.getHeader().getNumberOfRecords());
        byte[] expectedRecordData = { (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x01, (byte) 0xff, 0x00, 0x00, 0x00, 0x41, 0x6e, 0x6f, 0x74, 0x68, 0x65, 0x72, 0x20, 0x6f, 0x6e, 0x65, 0x00, 0x01, 0x30, 0x00, 0x02, 0x33, 0x00, 0x03, 0x66, 0x61, 0x6c, 0x73, 0x65, 0x00, 0x04, 0x33, 0x37, 0x35, 0x32, 0x38, 0x00, 0x05, 0x36, 0x35, 0x35, 0x38, 0x30, 0x00, 0x06, 0x49, 0x74, 0x65, 0x6d, 0x20, 0x6f, 0x6e, 0x65, 0x00, (byte) 0xff };
        Record expected = new RecordImpl(expectedRecordData);
        Record record = database.getRecords().get(4);
        assertEquals(expected, record);
        assertEquals(new UInt8(2), record.getHeader().getCategory());
        assertNull(database.getSortInfo());
    }
}
