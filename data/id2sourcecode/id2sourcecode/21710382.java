    public TableLayout parse(URL url) throws IOException {
        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = fact.newDocumentBuilder();
            return parse(docBuilder.parse(url.openStream()));
        } catch (ParserConfigurationException configExp) {
            configExp.printStackTrace();
            throw new IOException(configExp.getMessage());
        } catch (SAXException saxExp) {
            saxExp.printStackTrace();
            throw new IOException(saxExp.getMessage());
        }
    }
