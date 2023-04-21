    public void testWriteXmlLResultSet() throws Exception {
        StringWriter writer = new StringWriter();
        rs = st.executeQuery("select * from user_info");
        jrs.writeXml(rs, writer);
        JoinRowSet jrs2 = newJoinRowSet();
        jrs2.readXml(new StringReader(writer.getBuffer().toString()));
        assertCachedRowSetEquals(crset, jrs2);
    }
