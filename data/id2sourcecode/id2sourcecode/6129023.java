    public void parse(InputSource input) throws IOException, SAXException {
        ContentHandler ch = getContentHandler();
        if (ch == null) {
            return;
        }
        BufferedReader br = null;
        if (input.getCharacterStream() != null) {
            br = new BufferedReader(input.getCharacterStream());
        } else if (input.getByteStream() != null) {
            br = new BufferedReader(new InputStreamReader(input.getByteStream()));
        } else if (input.getSystemId() != null) {
            java.net.URL url = new URL(input.getSystemId());
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            throw new SAXException("Invalid InputSource object");
        }
        ch.startDocument();
        ch.startElement("", "", "csvFile", EMPTY_ATTR);
        String curLine = null;
        while ((curLine = br.readLine()) != null) {
            curLine = curLine.trim();
            if (curLine.length() > 0) {
                ch.startElement("", "", "line", EMPTY_ATTR);
                parseLine(curLine, ch);
                ch.endElement("", "", "line");
            }
        }
        ch.endElement("", "", "csvFile");
        ch.endDocument();
    }
