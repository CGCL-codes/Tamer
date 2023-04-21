    private List<String> copy(InputStream input, boolean readUpdates, File file, String[] updates) throws IOException, XMLStreamException {
        FileOutputStream output = new FileOutputStream(file);
        XMLEventReader reader = this.xmlInputFactory.createXMLEventReader(input);
        if (readUpdates) reader = new UpdatesXMLEventReader(reader);
        XMLEventWriter writer = this.xmlOutputFactory.createXMLEventWriter(output);
        if (updates != null) writer = new UpdatesXMLEventWriter(writer, Arrays.asList(updates));
        writer.add(reader);
        writer.close();
        output.close();
        return readUpdates ? ((UpdatesXMLEventReader) reader).getUpdates() : null;
    }
