    HtmlTablePanel(HtmlTableLayout owner, String urlStr) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {
        this(owner, HtmlTablePanel.class.getResource(urlStr).openStream());
    }
