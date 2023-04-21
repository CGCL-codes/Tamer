    public static void exampleContentHandlertoDOM(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        if (tfactory.getFeature(SAXSource.FEATURE) && tfactory.getFeature(DOMSource.FEATURE)) {
            SAXTransformerFactory sfactory = (SAXTransformerFactory) tfactory;
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            org.w3c.dom.Document outNode = docBuilder.newDocument();
            TransformerHandler handler = sfactory.newTransformerHandler(new StreamSource(xslID));
            handler.setResult(new DOMResult(outNode));
            XMLReader reader = makeXMLReader();
            reader.setContentHandler(handler);
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            reader.parse(sourceID);
            exampleSerializeNode(outNode);
        } else {
            System.out.println("Can't do exampleContentHandlerToContentHandler because tfactory is not a SAXTransformerFactory");
        }
    }
