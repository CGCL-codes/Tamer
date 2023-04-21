    public FormatRecognizer(URL url) {
        FormatRecognizerContentHandler handler = new FormatRecognizerContentHandler();
        try {
            XMLReader saxReader = XMLReaderFactory.createXMLReader();
            saxReader.setContentHandler(handler);
            saxReader.parse(new InputSource(url.openStream()));
        } catch (StopProcessingException e) {
            if (Namespaces.prefixToId.get("rdf").equals(handler.startURI)) {
                System.out.println("FormatRecognizer: " + url + ": root XML element: " + handler.startLocalName);
                rdfRoot = true;
            } else if (Namespaces.prefixToId.get("owl").equals(handler.startURI)) {
                System.out.println("FormatRecognizer: " + url + ": root XML element: " + handler.startLocalName);
                owlRoot = true;
            }
        } catch (Exception e) {
            throw new RuntimeException("N3SourceFromXML_Gloze.extractXMLNamespaces():\n" + "Could not load from URL " + url + "\n reason: " + e.getLocalizedMessage() + "\n cause: " + e.getCause(), e);
        }
    }
