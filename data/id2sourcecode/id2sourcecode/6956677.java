    public ValidatorResources(URL[] urls) throws IOException, SAXException {
        super();
        Digester digester = initDigester();
        for (int i = 0; i < urls.length; i++) {
            digester.push(this);
            InputStream stream = null;
            try {
                stream = urls[i].openStream();
                org.xml.sax.InputSource source = new org.xml.sax.InputSource(urls[i].toExternalForm());
                source.setByteStream(stream);
                digester.parse(source);
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
        this.process();
    }
