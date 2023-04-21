    public String readDocumentAsString(String filename) throws ConfigurationException {
        StringWriter writer = new StringWriter();
        try {
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(readDocument(filename), writer);
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }
        return writer.toString();
    }
