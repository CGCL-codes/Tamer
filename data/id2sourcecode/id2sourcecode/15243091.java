    public void parse(InputSource source) throws SAXException, IOException {
        systemId = source.getSystemId();
        contentHandler.setDocumentLocator(this);
        final Reader reader = source.getCharacterStream();
        try {
            if (reader == null) {
                InputStream stream = source.getByteStream();
                final String encoding = source.getEncoding();
                if (stream == null) {
                    systemId = source.getSystemId();
                    if (systemId == null) {
                        SAXParseException saxException = new SAXParseException("null source systemId", this);
                        errorHandler.fatalError(saxException);
                        return;
                    }
                    try {
                        final URL url = new URL(systemId);
                        stream = url.openStream();
                    } catch (MalformedURLException nue) {
                        try {
                            stream = new FileInputStream(systemId);
                        } catch (FileNotFoundException fnfe) {
                            final SAXParseException saxException = new SAXParseException("could not open file with systemId " + systemId, this, fnfe);
                            errorHandler.fatalError(saxException);
                            return;
                        }
                    }
                }
                pp.setInput(stream, encoding);
            } else {
                pp.setInput(reader);
            }
        } catch (XmlPullParserException ex) {
            final SAXParseException saxException = new SAXParseException("parsing initialization error: " + ex, this, ex);
            errorHandler.fatalError(saxException);
            return;
        }
        try {
            contentHandler.startDocument();
            pp.next();
            if (pp.getEventType() != XmlPullParser.START_TAG) {
                final SAXParseException saxException = new SAXParseException("expected start tag not" + pp.getPositionDescription(), this);
                errorHandler.fatalError(saxException);
                return;
            }
        } catch (XmlPullParserException ex) {
            final SAXParseException saxException = new SAXParseException("parsing initialization error: " + ex, this, ex);
            errorHandler.fatalError(saxException);
            return;
        }
        parseSubTree(pp);
        contentHandler.endDocument();
    }
