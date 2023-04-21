    public void test() throws Exception {
        XMLReader reader = new FlatXMLReader(getRoot());
        StringWriter writer = new StringWriter();
        generate(new SAXSource(reader, new InputSource(getFlat())), new StreamResult(writer));
        assertEqual(getXML(), writer.toString());
    }
