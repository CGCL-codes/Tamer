            public XMLStreamReader read(XMLInputFactory xif) throws IOException, XMLStreamException {
                InputStream is = url.openStream();
                return new TidyXMLStreamReader(xif.createXMLStreamReader(systemId.toExternalForm(), is), is);
            }
