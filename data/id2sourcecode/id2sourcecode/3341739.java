    @Override
    public Document parse(InputSource source) throws SAXException, IOException {
        if (source == null) {
            throw new IllegalArgumentException();
        }
        String namespaceURI = null;
        String qualifiedName = null;
        DocumentType doctype = null;
        String inputEncoding = source.getEncoding();
        String systemId = source.getSystemId();
        DocumentImpl document = new DocumentImpl(dom, namespaceURI, qualifiedName, doctype, inputEncoding);
        document.setDocumentURI(systemId);
        try {
            KXmlParser parser = new KXmlParser();
            parser.keepNamespaceAttributes();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, namespaceAware);
            if (source.getByteStream() != null) {
                parser.setInput(source.getByteStream(), inputEncoding);
            } else if (source.getCharacterStream() != null) {
                parser.setInput(source.getCharacterStream());
            } else if (systemId != null) {
                URL url = new URL(systemId);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                parser.setInput(urlConnection.getInputStream(), inputEncoding);
            } else {
                throw new SAXParseException("InputSource needs a stream, reader or URI", null);
            }
            if (parser.nextToken() == XmlPullParser.END_DOCUMENT) {
                throw new SAXParseException("Unexpected end of document", null);
            }
            parse(parser, document, document, XmlPullParser.END_DOCUMENT);
            parser.require(XmlPullParser.END_DOCUMENT, null, null);
        } catch (XmlPullParserException ex) {
            if (ex.getDetail() instanceof IOException) {
                throw (IOException) ex.getDetail();
            }
            if (ex.getDetail() instanceof RuntimeException) {
                throw (RuntimeException) ex.getDetail();
            }
            LocatorImpl locator = new LocatorImpl();
            locator.setPublicId(source.getPublicId());
            locator.setSystemId(systemId);
            locator.setLineNumber(ex.getLineNumber());
            locator.setColumnNumber(ex.getColumnNumber());
            SAXParseException newEx = new SAXParseException(ex.getMessage(), locator);
            if (errorHandler != null) {
                errorHandler.error(newEx);
            }
            throw newEx;
        }
        return document;
    }
