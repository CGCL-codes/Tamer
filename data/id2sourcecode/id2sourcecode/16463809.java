    public void invoke() throws IOException, XMLStreamException {
        try {
            _out.setOutput(_buffer);
            _writer.setOutput(_out);
            final XMLStreamWriter xmlOut = _writer.getStreamWriter();
            xmlOut.setPrefix(csq(ENVELOPE_PREFIX), csq(ENVELOPE_URI));
            xmlOut.writeStartElement(csq(ENVELOPE_URI), csq("Envelope"));
            xmlOut.writeNamespace(csq(ENVELOPE_PREFIX), csq(ENVELOPE_URI));
            xmlOut.writeStartElement(csq(ENVELOPE_URI), csq("Header"));
            xmlOut.writeEndElement();
            xmlOut.writeStartElement(csq(ENVELOPE_URI), csq("Body"));
            writeRequest(_writer);
            _writer.close();
            if (_url == null) throw new IOException("URL not set");
            java.net.HttpURLConnection http = (java.net.HttpURLConnection) ((java.net.URL) _url).openConnection();
            http.setRequestProperty("Content-Length", String.valueOf(_buffer.length()));
            http.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            _utf8Writer.setOutput(http.getOutputStream());
            _buffer.print(_utf8Writer);
            _utf8Writer.close();
            _reader.setInput(http.getInputStream());
            final XMLStreamReader xmlIn = _reader.getStreamReader();
            while (xmlIn.hasNext()) {
                if ((xmlIn.next() == XMLStreamReader.START_ELEMENT) && xmlIn.getLocalName().equals("Body") && xmlIn.getNamespaceURI().equals(ENVELOPE_URI)) {
                    xmlIn.next();
                    readResponse(_reader);
                    break;
                }
            }
        } finally {
            _reader.close();
            _writer.reset();
            _out.reset();
            _buffer.reset();
            _utf8Writer.reset();
            _reader.reset();
        }
    }
