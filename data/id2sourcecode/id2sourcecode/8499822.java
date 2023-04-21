    protected InputStream getInputStream(Source source) throws XMLStreamException {
        InputStream in = null;
        if (source instanceof StreamSource) {
            StreamSource streamSource = (StreamSource) source;
            in = streamSource.getInputStream();
        }
        if (in == null) {
            String systemId = source.getSystemId();
            try {
                URL url = new URL(systemId);
                try {
                    in = url.openStream();
                } catch (IOException e2) {
                    XMLStreamException e3 = new XMLStreamException(e2);
                    e3.initCause(e2);
                    throw e3;
                }
            } catch (MalformedURLException e) {
                if (File.separatorChar != '/') systemId = systemId.replace('/', File.separatorChar);
                try {
                    in = new FileInputStream(systemId);
                } catch (FileNotFoundException e2) {
                    XMLStreamException e3 = new XMLStreamException(e2);
                    e3.initCause(e2);
                    throw e3;
                }
            }
        }
        return in;
    }
