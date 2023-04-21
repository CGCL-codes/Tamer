    private static final Document _loadDocument(URL url) {
        if (url == null) {
            throw new JavalidException("Cannot load document url parameter is null");
        }
        DocumentBuilderFactory factory = null;
        DocumentBuilder builder = null;
        Document document = null;
        factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new CustomSaxErrorHandler());
            document = builder.parse(url.openConnection().getInputStream());
        } catch (ParserConfigurationException e) {
            throw new JavalidException("Parser error", e);
        } catch (SAXException e) {
            throw new JavalidException("Sax error", e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JavalidException("IO error", e);
        }
        return document;
    }
