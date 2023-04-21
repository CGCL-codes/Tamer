    protected XmlSchema customize(final XmlSchema schema, final String xsltFileName) throws TransformerException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StringWriter writer = new StringWriter();
        Source source = new DOMSource(schema.getAllSchemas()[0]);
        Result result = new StreamResult(writer);
        Source xsltSource = new StreamSource(new File(xsltFileName));
        Transformer transformer = tFactory.newTransformer(xsltSource);
        transformer.transform(source, result);
        writer.flush();
        StringReader reader = new StringReader(writer.toString());
        XmlSchemaCollection schemaCol = new XmlSchemaCollection();
        return schemaCol.read(reader, null);
    }
