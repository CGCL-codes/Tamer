    public static LibraryDesc getLibraryDesc(URL url) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domBuilder = factory.newDocumentBuilder();
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer trans = transFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            Document dom = domBuilder.parse(url.openStream());
            NodeList list = dom.getElementsByTagName("library");
            if (list.getLength() > 0) {
                Node descNode = list.item(0);
                NamedNodeMap attrs = descNode.getAttributes();
                LibraryDesc libDesc = new LibraryDesc();
                libDesc.setUrl(url);
                libDesc.setId(getNodeItemValue(attrs, "id"));
                libDesc.setName(getNodeItemValue(attrs, "name"));
                libDesc.setDescription(getNodeItemValue(attrs, "desc"));
                return libDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
