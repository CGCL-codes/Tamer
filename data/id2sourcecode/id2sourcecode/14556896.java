    public static List<String> getDataStreams(String dSUrl) throws org.osid.repository.RepositoryException {
        List<String> dataStreams = new ArrayList<String>();
        try {
            URL url = new URL(dSUrl);
            XPathFactory factory = XPathFactory.newInstance();
            XPath xPath = factory.newXPath();
            InputSource inputSource = new InputSource(url.openStream());
            NodeList dSNodes = (NodeList) xPath.evaluate("/objectDatastreams/datastream", inputSource, XPathConstants.NODESET);
            for (int i = 0; i < dSNodes.getLength(); i++) {
                Node n = dSNodes.item(i);
                dataStreams.add(n.getAttributes().getNamedItem(DS_ID_ATTRIBUTE).getNodeValue());
            }
        } catch (Throwable t) {
            throw wrappedException("getDataStreams", t);
        }
        return dataStreams;
    }
