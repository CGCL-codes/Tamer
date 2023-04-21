    @SuppressWarnings("unchecked")
    public String transform(String xml, Map mapParams) throws TransformerException, TransformerConfigurationException {
        StringReader reader = new StringReader(xml);
        StringWriter writer = new StringWriter();
        this.transform(new StreamSource(reader), new StreamResult(writer), mapParams);
        return writer.toString();
    }
