    @Test
    public void testMarshall() throws Exception {
        StringWriter writer = new StringWriter();
        TraffiscopeXO.marshal(TraffiscopeHelper.unmarshallTestData(), writer);
        writer.close();
        StringReader reader = new StringReader(writer.toString());
        TraffiscopeHelper.assertEquals(TraffiscopeHelper.TESTDATA, TraffiscopeXO.unmarshal(reader));
    }
