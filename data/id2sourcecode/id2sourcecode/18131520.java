    public void testWriteXml() throws Exception {
        StringWriter writer = new StringWriter();
        jrs.addRowSet(crset, 1);
        jrs.writeXml(writer);
        WebRowSet another = newWebRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        assertCachedRowSetEquals(jrs, another);
    }
