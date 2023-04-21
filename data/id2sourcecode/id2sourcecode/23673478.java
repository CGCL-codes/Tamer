    public static SDDocumentSource create(final URL url) {
        return new SDDocumentSource() {

            private final URL systemId = url;

            public XMLStreamReader read(XMLInputFactory xif) throws IOException, XMLStreamException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(xif.createXMLStreamReader(systemId.toExternalForm(), is), is);
            }

            public XMLStreamReader read() throws IOException, XMLStreamException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(XMLStreamReaderFactory.create(systemId.toExternalForm(), is, false), is);
            }

            public URL getSystemId() {
                return systemId;
            }
        };
    }
