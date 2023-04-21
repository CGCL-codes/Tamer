    public void parse(InputSource source) throws SAXException, IOException {
        if (handler == null) {
            return;
        }
        this.source = source;
        Reader r = source.getCharacterStream();
        if (r == null) {
            InputStream in = source.getByteStream();
            if (in == null) {
                String uri = source.getSystemId();
                if (uri == null) {
                    throw new SAXException("Bad InputSource");
                }
                URL url = new File(uri).isFile() ? new URL("file", null, uri) : new URL(uri);
                in = url.openStream();
            }
            r = detectEncoding(in);
        }
        if (!(r instanceof BufferedReader)) {
            r = new BufferedReader(r);
        }
        lineNumber = 1;
        hadCR = false;
        handler.startDocument();
        int c;
        if (!startTagStarted) {
            if ('<' != skipWhitespace(r)) {
                reportError("Expecting a \'<\' at document start");
            }
            c = readChar(r);
        } else {
            c = (firstNameChar == 0) ? readChar(r) : firstNameChar;
        }
        parseElement(r, c);
        if (skipWhitespace(r) >= 0) {
            reportError("Extra chars at document end");
        }
        handler.endDocument();
    }
