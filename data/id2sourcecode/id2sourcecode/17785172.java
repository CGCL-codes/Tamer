    public void testWriteAndRead_Insert() throws Exception {
        rs = st.executeQuery("SELECT * FROM USER_INFO");
        webRs = newWebRowSet();
        webRs.populate(rs);
        assertTrue(webRs.next());
        webRs.moveToInsertRow();
        webRs.updateInt(1, 5);
        webRs.updateString(2, "insertrow");
        webRs.insertRow();
        webRs.moveToCurrentRow();
        webRs.beforeFirst();
        StringWriter writer = new StringWriter();
        webRs.writeXml(writer);
        another = newWebRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        assertCachedRowSetEquals(webRs, another);
    }
