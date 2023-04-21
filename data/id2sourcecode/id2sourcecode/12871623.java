    private void writeNumber(StartElement start, XMLEventReader reader, PrintWriter writer) throws XMLStreamException {
        String chars;
        Attribute v = start.getAttributeByName(kATTR_VALUE);
        if (v != null) chars = v.getValue(); else chars = readChars(reader);
        chars = chars.trim();
        writer.print(chars);
        readToEnd(reader);
    }
