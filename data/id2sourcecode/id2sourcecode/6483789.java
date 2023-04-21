    public static GBObject getGBObject(String id, ObjectType type) throws ParserConfigurationException, FactoryConfigurationError, SAXException, IOException {
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        URL url = new URL("http://api.giantbomb.com/" + type.toString().toLowerCase() + "/" + id + "/?api_key=" + API_KEY + "&field_list=id,name,deck,description,site_detail_url,games,platforms,franchises,characters,concepts,objects,locations,people,companies&format=xml");
        Document doc = docBuilder.parse(url.openConnection().getInputStream());
        return parseObject((Element) doc.getElementsByTagName("results").item(0), type);
    }
