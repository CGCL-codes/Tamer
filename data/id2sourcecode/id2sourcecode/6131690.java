    @Override
    protected void _outputFromBO(Object busObject, Object busContext, XMLStreamWriter writer) throws XMLStreamException, WebServiceException {
        XMLStreamReader reader = _getReaderFromBO(busObject, busContext);
        _outputFromReader(reader, writer);
    }
