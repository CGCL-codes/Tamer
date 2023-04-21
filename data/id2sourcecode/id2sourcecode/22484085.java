    public static Document parse(URL url, Map properties) {
        Document document = null;
        InputStream xmlIO = null;
        if (url != null) {
            try {
                xmlIO = url.openStream();
                InputSource inputSource = new InputSource(xmlIO);
                XMLContentHandler handler = new XMLContentHandler(properties);
                XMLReader xmlReader = (XMLReader) xmlReaderPool.remove();
                if (xmlReader == null) {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    factory.setNamespaceAware(false);
                    factory.setValidating(false);
                    xmlReader = factory.newSAXParser().getXMLReader();
                }
                xmlReader.setContentHandler(handler);
                xmlReader.parse(inputSource);
                xmlReaderPool.add(xmlReader);
                document = handler.getDocument();
                xmlIO.close();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }
