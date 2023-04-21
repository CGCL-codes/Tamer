    protected void _outputFromBO(Object busObject, Object busContext, XMLStreamWriter writer) throws XMLStreamException {
        XMLStreamReader reader = _getReaderFromBO(busObject, busContext);
        _outputFromReader(reader, writer);
    }
