    public void loadCatalogfromConfig(String filename, CswProfiles profileList) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        if ((filename == null) || (filename.length() == 0)) {
            Properties properties = new Properties();
            final URL url = CswCatalogs.class.getResource("CswCommon.properties");
            properties.load(url.openStream());
            filename = properties.getProperty("DEFAULT_CONFIGURATION_FOLDER_PATH");
            filename += properties.getProperty("DEFAULT_CATALOG_FILE");
        }
        String CATALOG_TAG = "CSWCatalog";
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(filename);
        NodeList nodes = doc.getElementsByTagName(CATALOG_TAG);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node currNode = nodes.item(i);
            XPath xpath = XPathFactory.newInstance().newXPath();
            String url = xpath.evaluate("URL", currNode);
            String name = xpath.evaluate("Name", currNode);
            String profileId = xpath.evaluate("CSWProfile", currNode);
            String userName = xpath.evaluate("Credentials/Username", currNode);
            String password = xpath.evaluate("Credentials/Password", currNode);
            CswProfile profile = profileList.getProfileById(profileId);
            CswCatalog catalog = new CswCatalog(url, name, profile);
            addCatalog(catalog);
        }
    }
