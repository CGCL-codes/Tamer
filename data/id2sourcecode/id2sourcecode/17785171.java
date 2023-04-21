    public void testWriteAndRead() throws Exception {
        rs = st.executeQuery("SELECT * FROM USER_INFO");
        webRs = newWebRowSet();
        webRs.populate(rs);
        StringWriter writer = new StringWriter();
        webRs.writeXml(writer);
        another = newWebRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        assertCachedRowSetEquals(webRs, another);
    }
