    private XMLElement getXMLElementFromURL() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, XMLException {
        XMLElement el;
        String xmlText = "";
        Reader st = new InputStreamReader(url.openStream());
        int ch = st.read();
        while (ch != -1) {
            char[] cbuf = new char[1];
            cbuf[0] = (char) ch;
            xmlText = xmlText + new String(cbuf);
            ch = st.read();
        }
        IXMLParser parser = XMLParserFactory.createDefaultXMLParser();
        Reader breader = new StringReader(xmlText);
        IXMLReader reader = new StdXMLReader(breader);
        parser.setReader(reader);
        el = (XMLElement) parser.parse();
        return el;
    }
