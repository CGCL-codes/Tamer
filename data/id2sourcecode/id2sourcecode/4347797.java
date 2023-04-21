    public void test() throws Exception {
        FlatStreamReader reader = new FlatStreamReader(getRoot(), new InputStreamReader(getFlat()));
        StringWriter writer = new StringWriter();
        generate(new StAXSource(reader), new StreamResult(writer));
        assertEqual(getXML(), writer.toString());
    }
