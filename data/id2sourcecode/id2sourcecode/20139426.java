    public void testBug1180791() throws Exception {
        String xml = "<?xml version=\"1.0\"?><root><foo>bar</foo></root>";
        SAXReader reader = new SAXReader();
        Document doc = reader.read(new StringReader(xml));
        OutputFormat format = new OutputFormat();
        format.setNewlines(true);
        StringWriter writer = new StringWriter();
        XMLWriter xmlwriter = new XMLWriter(writer, format);
        xmlwriter.write(doc);
        System.out.println(writer.toString());
        doc = reader.read(new StringReader(writer.toString()));
        writer = new StringWriter();
        xmlwriter = new XMLWriter(writer, format);
        xmlwriter.write(doc);
        System.out.println(writer.toString());
    }
