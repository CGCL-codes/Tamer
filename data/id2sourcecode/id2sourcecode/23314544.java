    private void writeXML(Properties props, String comment) throws IOException, CoreException, SaxonApiException, XMLStreamException {
        SerializeOpts serializeOpts = getSerializeOpts();
        ByteArrayOutputStream oss = new ByteArrayOutputStream();
        props.storeToXML(oss, comment, serializeOpts.getOutputXmlEncoding());
        ByteArrayInputStream iss = new ByteArrayInputStream(oss.toByteArray());
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.valueOf(false));
        XMLEventReader reader = factory.createXMLEventReader(null, iss);
        XMLEventWriter writer = getStdout().asXMLEventWriter(serializeOpts);
        writer.add(reader);
        reader.close();
        writer.close();
    }
