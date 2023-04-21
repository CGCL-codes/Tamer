    protected void _outputFromReader(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        Reader2Writer r2w = new Reader2Writer(reader);
        r2w.outputTo(writer);
    }
