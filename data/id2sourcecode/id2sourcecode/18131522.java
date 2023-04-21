    public void testWriteAndRead_Update() throws Exception {
        jrs.addRowSet(crset, 1);
        jrs.beforeFirst();
        assertTrue(jrs.absolute(3));
        jrs.updateString(2, "updateRow");
        jrs.updateRow();
        assertTrue(jrs.next());
        jrs.updateString(2, "anotherUpdateRow");
        jrs.updateRow();
        StringWriter writer = new StringWriter();
        jrs.writeXml(writer);
        JoinRowSet another = newJoinRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        if (System.getProperty("Testing Harmony") == "true") {
            assertCachedRowSetEquals(jrs, another);
        } else {
            another.absolute(3);
            assertFalse(another.rowUpdated());
            jrs.absolute(3);
            assertTrue(jrs.rowUpdated());
            another.absolute(4);
            assertFalse(another.rowUpdated());
            jrs.absolute(4);
            assertTrue(jrs.rowUpdated());
        }
    }
