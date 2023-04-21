    private static XMLEventReader _s_openEventReaderURL(URL url, boolean isNamespaceAware) throws IOException, XMLStreamException {
        InputStream inputStream = url.openStream();
        return _s_openEventReaderStream(inputStream, isNamespaceAware);
    }
