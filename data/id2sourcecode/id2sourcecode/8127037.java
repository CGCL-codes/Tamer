    public static void main(String[] args) {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(false);
        try {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLReader _reader = saxParser.getXMLReader();
            _reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
            XMLWriter _writer = new XMLHandlerWriter();
            _writer.setEncoding("UTF-8");
            OutputStream _outstream = new FileOutputStream(args[1]);
            _writer.setOutputStream(_outstream);
            _reader.setContentHandler(_writer.getContentHandler());
            _reader.parse(new org.xml.sax.InputSource(args[0]));
            _outstream.flush();
            _outstream.close();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
