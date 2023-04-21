    private void writeNull(XMLEventReader reader, PrintWriter writer) throws XMLStreamException {
        writer.print("null");
        readToEnd(reader);
    }
