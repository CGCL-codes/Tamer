            public XMLStreamReader read() throws IOException, XMLStreamException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(XMLStreamReaderFactory.create(systemId.toExternalForm(), is, false), is);
            }
