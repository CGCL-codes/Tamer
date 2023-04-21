    public void testWriteAndRead_Insert() throws Exception {
        jrs.addRowSet(crset, 1);
        jrs.beforeFirst();
        assertTrue(jrs.next());
        jrs.moveToInsertRow();
        jrs.updateInt(1, 5);
        jrs.updateString(2, "insertrow");
        jrs.insertRow();
        jrs.moveToCurrentRow();
        jrs.beforeFirst();
        jrs.absolute(2);
        assertTrue(jrs.rowInserted());
        StringWriter writer = new StringWriter();
        jrs.writeXml(writer);
        JoinRowSet another = newJoinRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        if (System.getProperty("Testing Harmony") == "true") {
            assertCachedRowSetEquals(jrs, another);
        } else {
            another.absolute(2);
            assertFalse(another.rowInserted());
            jrs.absolute(2);
            assertTrue(jrs.rowInserted());
        }
    }
