    private OMNode convertToOM(XmlSchema schema) throws DataRetrievalException {
        StringWriter writer = new StringWriter();
        schema.write(writer);
        StringReader reader = new StringReader(writer.toString());
        try {
            return XMLUtils.toOM(reader);
        } catch (XMLStreamException e) {
            throw new DataRetrievalException("Can't convert XmlSchema object to an OMElement", e);
        }
    }
