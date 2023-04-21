    public void transform(Reader reader, Writer writer) throws Exception {
        Source xmlSource = new StreamSource(reader);
        Result outputTarget = new StreamResult(writer);
        transformer.transform(xmlSource, outputTarget);
    }
