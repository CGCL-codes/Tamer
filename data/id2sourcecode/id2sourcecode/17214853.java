    public void format(Reader reader, Writer writer, String cssClass, String urlContext) throws SAXException, IOException {
        try {
            _cssClass = cssClass;
            _urlContext = urlContext;
            _writer = writer;
            _reader = new org.apache.xerces.parsers.SAXParser();
            _reader.setContentHandler(this);
            _reader.setErrorHandler(this);
            _reader.parse(new InputSource(reader));
        } finally {
            _reader = null;
            _writer = null;
            _cssClass = null;
        }
    }
