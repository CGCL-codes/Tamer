    private Document parseXmlResource(final String resourcePath) throws SAXParseException, UnableToCompleteException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setExpandEntityReferences(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        try {
            ClassLoader classLoader = UiBinderGenerator.class.getClassLoader();
            URL url = classLoader.getResource(resourcePath);
            if (null == url) {
                die("Unable to find resource: " + resourcePath);
            }
            InputStream stream = url.openStream();
            InputSource input = new InputSource(stream);
            input.setSystemId(url.toExternalForm());
            return builder.parse(input);
        } catch (SAXParseException e) {
            throw e;
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
