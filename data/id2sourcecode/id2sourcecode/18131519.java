    public void testReadXml_Empty() throws Exception {
        jrs = newJoinRowSet();
        jrs.addRowSet(crset, 1);
        StringWriter writer = new StringWriter();
        jrs.writeXml(writer);
        JoinRowSet another = newJoinRowSet();
        another.readXml(new StringReader(writer.getBuffer().toString()));
        assertCachedRowSetEquals(jrs, another);
    }
