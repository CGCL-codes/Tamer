    public static void exampleContentHandler2DOM(String sourceID, String xslID) throws TransformerException, TransformerConfigurationException, SAXException, IOException, ParserConfigurationException {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        if (tfactory.getFeature(SAXSource.FEATURE) && tfactory.getFeature(DOMSource.FEATURE)) {
            SAXTransformerFactory sfactory = (SAXTransformerFactory) tfactory;
            DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
            org.w3c.dom.Document outNode = docBuilder.newDocument();
            TransformerHandler handler = sfactory.newTransformerHandler(new StreamSource(xslID));
            handler.setResult(new DOMResult(outNode));
            XMLReader reader = null;
            try {
                javax.xml.parsers.SAXParserFactory factory = javax.xml.parsers.SAXParserFactory.newInstance();
                factory.setNamespaceAware(true);
                javax.xml.parsers.SAXParser jaxpParser = factory.newSAXParser();
                reader = jaxpParser.getXMLReader();
            } catch (javax.xml.parsers.ParserConfigurationException ex) {
                throw new org.xml.sax.SAXException(ex);
            } catch (javax.xml.parsers.FactoryConfigurationError ex1) {
                throw new org.xml.sax.SAXException(ex1.toString());
            } catch (NoSuchMethodError ex2) {
            }
            if (reader == null) reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.setProperty("http://xml.org/sax/properties/lexical-handler", handler);
            reader.parse(sourceID);
            exampleSerializeNode(outNode);
        } else {
            System.out.println("Can't do exampleContentHandlerToContentHandler because tfactory is not a SAXTransformerFactory");
        }
    }
