    private Set<String> parseXmlList(URL url, String tagName, DataStructureType type) throws SAXException, IOException {
        InputStream inStream = url.openStream();
        Document doc = builder.parse(inStream);
        Element root = doc.getDocumentElement();
        TreeSet<String> strSet = new TreeSet<String>();
        NodeList nodeList = root.getElementsByTagName(tagName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            String name = ((Element) nodeList.item(i)).getAttribute("name");
            strSet.add(name);
            typeMap.put(name, type);
        }
        inStream.close();
        return strSet;
    }
